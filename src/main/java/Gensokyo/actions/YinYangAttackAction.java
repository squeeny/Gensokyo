package Gensokyo.actions;

import Gensokyo.powers.Position;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class YinYangAttackAction extends AbstractGameAction {
    int position;
    DamageInfo damage;

    public YinYangAttackAction(int position, DamageInfo damage) {
        this.actionType = ActionType.DAMAGE;
        this.duration = Settings.ACTION_DUR_FAST;
        this.position = position;
        this.damage = damage;
    }

    public void update() {
        this.isDone = false;
        int playerPosition = 1;
        if (AbstractDungeon.player.hasPower(Position.POWER_ID)) {
            playerPosition = AbstractDungeon.player.getPower(Position.POWER_ID).amount;
        }
        System.out.println(playerPosition);
        System.out.println(position);
        if (playerPosition == this.position) {
            System.out.println("blah");
            AbstractDungeon.actionManager.addToTop(new DamageAction(AbstractDungeon.player, damage, AttackEffect.FIRE, true));
        }
        this.isDone = true;
    }
}

