package Gensokyo.monsters.act2;

import Gensokyo.powers.act2.Reflowering;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class YuyukoSoul extends CustomMonster {
    protected static final byte NONE = 0;
    protected static final byte DEBUFF = 1;
    protected static final int DEBUFF_AMT = 1;
    protected static final int HP = 6;
    private static final float HB_W = 50.0F;
    private static final float HB_H = 100.0f;
    public boolean active = false;
    protected int cooldown;
    protected Yuyuko master;

    public YuyukoSoul(String name, String id, int maxHealth, float hb_x, float hb_y, String imgUrl, float offsetX, float offsetY, Yuyuko master, int bonusHealth) {
        super(name, id, maxHealth, hb_x, hb_y, HB_W, HB_H, imgUrl, offsetX, offsetY);
        this.master = master;
        this.setHp(HP + bonusHealth);
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.cooldown = 0;
        } else {
            this.cooldown = 1;
        }
    }

    @Override
    public void usePreBattleAction() {
        this.halfDead = true;
        this.currentHealth = 0;
        this.healthBarUpdatedEvent();
    }

    @Override
    public void render(SpriteBatch sb) {

    }

    public void realRender(SpriteBatch sb) {
        super.render(sb);
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(false);
        if (master.hasPower(Reflowering.POWER_ID)) {
            master.getPower(Reflowering.POWER_ID).onSpecificTrigger();
        }
        if (this instanceof BlueSoul) {
            master.blueSouls.remove(this);
            master.nextBlueSoul();
        }
        if (this instanceof PurpleSoul) {
            master.purpleSouls.remove(this);
            master.nextPurpleSoul();
        }
    }

    @Override
    public void takeTurn() {
        if (active && cooldown > 0) {
            cooldown--;
        }
    }

    @Override
    protected void getMove(int num) {
        if (!active) {
            this.setMove(NONE, Intent.NONE);
        } else if (cooldown > 0) {
            this.setMove(NONE, Intent.NONE);
        } else {
            this.setMove(DEBUFF, Intent.DEBUFF);
        }
    }
}