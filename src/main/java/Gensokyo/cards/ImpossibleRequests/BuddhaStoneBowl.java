package Gensokyo.cards.ImpossibleRequests;

import Gensokyo.GensokyoMod;
import Gensokyo.cards.AbstractDefaultCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static Gensokyo.GensokyoMod.makeCardPath;

public class BuddhaStoneBowl extends AbstractDefaultCard {

    public static final String ID = GensokyoMod.makeID(BuddhaStoneBowl.class.getSimpleName());
    public static final String IMG = makeCardPath("BuddhaStoneBowl.png");

    private static final CardRarity RARITY = CardRarity.CURSE;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = -2;

    public BuddhaStoneBowl() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
    }

    @Override
    public float getTitleFontSize()
    {
        return 18;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            initializeDescription();
        }
    }
}
