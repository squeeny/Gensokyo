package Gensokyo.monsters;

import Gensokyo.actions.InvertPowersAction;
import Gensokyo.powers.UnstableBoundariesPower;
import Gensokyo.vfx.EmptyEffect;
import Gensokyo.vfx.YukariTrainEffect;
import basemod.abstracts.CustomMonster;
import basemod.animations.SpriterAnimation;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Yukari extends CustomMonster
{
    public static final String ID = "Gensokyo:Yukari";
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final Texture TRAIN_INTENT_TEXTURE = ImageMaster.loadImage("GensokyoResources/images/monsters/Yukari/attack_intent_train.png");
    private boolean firstMove = true;
    private static final byte OPENING = 1;
    private static final byte STRENGTH_DRAIN = 2;
    private static final byte MEGA_DEBUFF = 3;
    private static final byte ATTACK = 4;
    private static final byte LAST_WORD = 5;
    private static final byte TRAIN = 6;
    private static final int NORMAL_ATTACK_DAMAGE = 10;
    private static final int A4_NORMAL_ATTACK_DAMAGE = 11;
    private static final int NORMAL_ATTACK_HITS = 2;
    private static final int DEBUFF_ATTACK_DAMAGE = 14;
    private static final int A4_DEBUFF_ATTACK_DAMAGE = 16;
    private static final int TRAIN_ATTACK_DAMAGE = 8;
    private static final int A4_TRAIN_ATTACK_DAMAGE = 9;
    private static final int TRAIN_ATTACK_HITS = 3;
    private static final int DEBUFF_AMOUNT = 2;
    private static final int A19_DEBUFF_AMOUNT = 3;
    private static final int STRENGTH_DRAIN_AMOUNT = 2;
    private static final int A19_STRENGTH_DRAIN_AMOUNT = 3;
    private static final int WOUND_AMOUNT = 1;
    private static final int A19_WOUND_AMOUNT = 2;
    private static final int BLOCK = 12;
    private static final int A9_BLOCK = 15;
    private int normalDamage;
    private int debuffDamage;
    private int trainDamage;
    private int debuffAmount;
    private int strengthDrain;
    private int block;
    private int wound;
    private boolean useTrain = false;
    private boolean useTrainTexture = false;
    private static final int HP = 245;
    private static final int A9_HP = 255;

    public Yukari() {
        this(0.0f, 0.0f);
    }

    public Yukari(final float x, final float y) {
        super(Yukari.NAME, ID, HP, -5.0F, 0, 280.0f, 285.0f, null, x, y);
        this.animation = new SpriterAnimation("GensokyoResources/images/monsters/Yukari/Spriter/YukariAnimations.scml");
        this.type = EnemyType.BOSS;
        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 19) {
            this.debuffAmount = A19_DEBUFF_AMOUNT;
            this.strengthDrain = A19_STRENGTH_DRAIN_AMOUNT;
            this.wound = A19_WOUND_AMOUNT;
        } else {
            this.debuffAmount = DEBUFF_AMOUNT;
            this.strengthDrain = STRENGTH_DRAIN_AMOUNT;
            this.wound = WOUND_AMOUNT;
        }
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(A9_HP);
            this.block = A9_BLOCK;
        } else {
            this.setHp(HP);
            this.block = BLOCK;
        }

        if (AbstractDungeon.ascensionLevel >= 4) {
            this.normalDamage = A4_NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = A4_DEBUFF_ATTACK_DAMAGE;
            this.trainDamage = A4_TRAIN_ATTACK_DAMAGE;
        } else {
            this.normalDamage = NORMAL_ATTACK_DAMAGE;
            this.debuffDamage = DEBUFF_ATTACK_DAMAGE;
            this.trainDamage = TRAIN_ATTACK_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, this.normalDamage));
        this.damage.add(new DamageInfo(this, this.trainDamage));
        this.damage.add(new DamageInfo(this, this.debuffDamage));
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("Gensokyo/Necrofantasia.mp3");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new UnstableBoundariesPower(this)));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case OPENING: {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, Yukari.DIALOG[0]));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strengthDrain), this.strengthDrain));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -this.strengthDrain), -this.strengthDrain));
                break;
            }
            case STRENGTH_DRAIN: {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.strengthDrain), this.strengthDrain));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new StrengthPower(AbstractDungeon.player, -this.strengthDrain), -this.strengthDrain));
                break;
            }
            case MEGA_DEBUFF: {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Wound(), this.wound));
                break;
            }
            case ATTACK: {
                for (int i = 0; i < NORMAL_ATTACK_HITS; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VulnerablePower(this, this.debuffAmount, true), this.debuffAmount));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakPower(this, this.debuffAmount, true), this.debuffAmount));
                break;
            }
            case LAST_WORD: {
                AbstractDungeon.actionManager.addToBottom(new InvertPowersAction(this, true));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, this.block));
                break;
            }
            case TRAIN: {
                for (int i = 0; i < TRAIN_ATTACK_HITS; i++) {
                    if (i == 0) {
                        AbstractDungeon.actionManager.addToBottom(new SFXAction("Gensokyo:Train"));
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new YukariTrainEffect(), 0.5F));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new VFXAction(new EmptyEffect(), 0.6F));
                    }
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new InvertPowersAction(this, true));
                useTrainTexture = false;
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(final int num) {
        if (this.firstMove) {
            this.setMove(Yukari.MOVES[0], OPENING, Intent.UNKNOWN);
            this.firstMove = false;
        }  else if (this.currentHealth < this.maxHealth / 2 && !this.useTrain) {
            this.useTrain = true;
            this.setMove(Yukari.MOVES[5], LAST_WORD, Intent.DEFEND_BUFF);
        } else if (this.lastMove(LAST_WORD)) {
            useTrainTexture = true;
            this.setMove(Yukari.MOVES[4], TRAIN, Intent.ATTACK_BUFF, (this.damage.get(1)).base, TRAIN_ATTACK_HITS, true);
        } else {
            if (this.useTrain && !this.lastMove(TRAIN) && !this.lastMoveBefore(TRAIN)) { //use train every 3 turns
                useTrainTexture = true;
                this.setMove(Yukari.MOVES[4], TRAIN, Intent.ATTACK_BUFF, (this.damage.get(1)).base, TRAIN_ATTACK_HITS, true);
            }
            else if (num < 35) {
                if (!this.lastMove(STRENGTH_DRAIN)) {
                    this.setMove(Yukari.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(2)).base);
                } else {
                    if (num % 2 == 0) {
                        this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                    } else {
                        this.setMove(Yukari.MOVES[2], MEGA_DEBUFF, Intent.DEFEND_DEBUFF);
                    }
                }
            } else if (num < 65) {
                if (!this.lastMove(MEGA_DEBUFF)) {
                    this.setMove(Yukari.MOVES[2], MEGA_DEBUFF, Intent.DEFEND_DEBUFF);
                } else {
                    if (num % 2 == 0) {
                        this.setMove(Yukari.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
                    } else {
                        this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                    }
                }
            } else {
                if (!this.lastMove(ATTACK)) {
                    this.setMove(Yukari.MOVES[3], ATTACK, Intent.ATTACK, (this.damage.get(0)).base, NORMAL_ATTACK_HITS, true);
                } else {
                    if (num % 2 == 0) {
                        this.setMove(Yukari.MOVES[2], MEGA_DEBUFF, Intent.DEFEND_DEBUFF);
                    } else {
                        this.setMove(Yukari.MOVES[1], STRENGTH_DRAIN, Intent.ATTACK_DEBUFF, (this.damage.get(1)).base);
                    }
                }
            }
        }
    }

    @Override
    public Texture getAttackIntent() {
        if (useTrainTexture) {
            return TRAIN_INTENT_TEXTURE;
        }
        return super.getAttackIntent();
    }
    @Override
    public Texture getAttackIntent(int dmg) {
        if (useTrainTexture) {
            return TRAIN_INTENT_TEXTURE;
        }
        return super.getAttackIntent(dmg);
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Gensokyo:Yukari");
        NAME = Yukari.monsterStrings.NAME;
        MOVES = Yukari.monsterStrings.MOVES;
        DIALOG = Yukari.monsterStrings.DIALOG;
    }
}