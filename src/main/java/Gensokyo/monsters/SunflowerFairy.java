package Gensokyo.monsters;

import Gensokyo.BetterSpriterAnimation;
import Gensokyo.powers.FairyFury;
import Gensokyo.powers.Immortality;
import basemod.abstracts.CustomMonster;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.Iterator;

public class SunflowerFairy extends CustomMonster
{
    public static final String ID = "Gensokyo:SunflowerFairy";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final byte ATTACK = 1;
    private static final byte REVIVE = 2;
    private static final int NORMAL_ATTACK_DAMAGE = 2;
    private static final int DEBUFF = 1;
    private static final int HP_MIN = 7;
    private static final int HP_MAX = 8;
    private static final int A_2_HP_MIN = 8;
    private static final int A_2_HP_MAX = 9;
    private int normalDamage;
    private Cirno leader;

    public SunflowerFairy() {
        this(0.0f, 0.0f, null);
    }

    public SunflowerFairy(final float x, final float y, Cirno leader) {
        super(SunflowerFairy.NAME, ID, HP_MAX, -5.0F, 0, 170.0f, 165.0f, null, x, y);
        this.animation = new BetterSpriterAnimation("GensokyoResources/images/monsters/Cirno/Spriter/CirnoAnimation.scml");
        this.type = EnemyType.NORMAL;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        this.normalDamage = NORMAL_ATTACK_DAMAGE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(A_2_HP_MIN, A_2_HP_MAX);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }

        this.damage.add(new DamageInfo(this, this.normalDamage));
        this.leader = leader;
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MinionPower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new Immortality(this)));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, DEBUFF, true), DEBUFF));
                break;
            }
            case REVIVE: {
                AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, this.maxHealth));
                this.halfDead = false;
                Iterator var1 = AbstractDungeon.player.relics.iterator();
                while(var1.hasNext()) {
                    AbstractRelic r = (AbstractRelic)var1.next();
                    r.onSpawnMonster(this);
                }
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.halfDead) {
            this.setMove(REVIVE, Intent.BUFF);
        } else {
            this.setMove(ATTACK, Intent.ATTACK_DEBUFF, (this.damage.get(0)).base);
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (this.currentHealth <= 0 && !this.halfDead) {
            this.halfDead = true;
            if (leader != null) {
                if (leader.hasPower(FairyFury.POWER_ID)) {
                    FairyFury fury = (FairyFury)leader.getPower(FairyFury.POWER_ID);
                    fury.onTrigger();
                }
            }
            Iterator var2 = this.powers.iterator();

            while (var2.hasNext()) {
                AbstractPower p = (AbstractPower) var2.next();
                p.onDeath();
            }

            var2 = AbstractDungeon.player.relics.iterator();

            while (var2.hasNext()) {
                AbstractRelic r = (AbstractRelic) var2.next();
                r.onMonsterDeath(this);
            }
            if (this.nextMove != REVIVE) {
                this.setMove(REVIVE, Intent.BUFF);
                this.createIntent();
                AbstractDungeon.actionManager.addToBottom(new SetMoveAction(this, REVIVE, Intent.BUFF));
            }
        }
    }

    public void die() {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die();
        }
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:SunflowerFairy");
        NAME = SunflowerFairy.monsterStrings.NAME;
        MOVES = SunflowerFairy.monsterStrings.MOVES;
        DIALOG = SunflowerFairy.monsterStrings.DIALOG;
    }
}