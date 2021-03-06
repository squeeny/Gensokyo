package Gensokyo.powers.act3;

import Gensokyo.GensokyoMod;
import Gensokyo.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.abstracts.TwoAmountPower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makePowerPath;

public class SarielEventPower extends TwoAmountPower {
    public static final String POWER_ID = GensokyoMod.makeID("SarielEventPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    private AbstractCard.CardType cardtype;

    private static final Texture tex84 = TextureLoader.getTexture(makePowerPath("IllusionaryDominance84.png"));
    private static final Texture tex32 = TextureLoader.getTexture(makePowerPath("IllusionaryDominance32.png"));

    public SarielEventPower(AbstractCreature owner, int threshold, AbstractCard.CardType cardtype) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.cardtype = cardtype;
        this.amount2 = 0;
        this.amount = threshold;
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);
        updateDescription();
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == cardtype && !card.purgeOnUse) {
            amount2++;
            if (this.amount2 >= amount) {
                this.flash();
                AbstractMonster m = null;
                if (action.target != null) {
                    m = (AbstractMonster)action.target;
                }

                AbstractCard tmp = card.makeSameInstanceOf();
                AbstractDungeon.player.limbo.addToBottom(tmp);
                tmp.current_x = card.current_x;
                tmp.current_y = card.current_y;
                tmp.target_x = (float) Settings.WIDTH / 2.0F - 300.0F * Settings.scale;
                tmp.target_y = (float)Settings.HEIGHT / 2.0F;
                if (m != null) {
                    tmp.calculateCardDamage(m);
                }

                tmp.purgeOnUse = true;
                AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(tmp, m, card.energyOnUse, true, true), true);
                amount2 = 0;
            }
        }
    }

    @Override
    public void updateDescription() {
        if (cardtype == AbstractCard.CardType.ATTACK) {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1] + DESCRIPTIONS[3];
        } else {
            description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2] + DESCRIPTIONS[3];
        }
    }
}
