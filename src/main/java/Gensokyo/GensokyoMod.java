package Gensokyo;

import Gensokyo.cards.Apocalypse;
import Gensokyo.cards.CrescentMoonSlash;
import Gensokyo.cards.EightFeetTall;
import Gensokyo.cards.GapWoman;
import Gensokyo.cards.HAARP;
import Gensokyo.cards.Kunekune;
import Gensokyo.cards.LittleGreenMen;
import Gensokyo.cards.LochNessMonster;
import Gensokyo.cards.ManorOfTheDishes;
import Gensokyo.cards.MenInBlack;
import Gensokyo.cards.MissMary;
import Gensokyo.cards.MonkeysPaw;
import Gensokyo.cards.RedCapeBlueCape;
import Gensokyo.cards.SevenSchoolMysteries;
import Gensokyo.cards.SlitMouthedWoman;
import Gensokyo.cards.SpontaneousHumanCombustion;
import Gensokyo.cards.TekeTeke;
import Gensokyo.cards.TurboGranny;
import Gensokyo.dungeon.Gensokyo;
import Gensokyo.events.ACelestialsPlight;
import Gensokyo.events.AHoleInReality;
import Gensokyo.events.ALandWhereOnlyIAmMissing;
import Gensokyo.events.ASwiftSlash;
import Gensokyo.events.BambooForestOfTheLost;
import Gensokyo.events.BorderOfDeath;
import Gensokyo.events.ClashOfLegends;
import Gensokyo.events.FieldTripToAnotherWorld;
import Gensokyo.events.ForestOfMagic;
import Gensokyo.events.GardenOfTheSun;
import Gensokyo.events.GoddessOfMisfortune;
import Gensokyo.events.GoodsFromTheOutsideWorld;
import Gensokyo.events.HakureiShrine;
import Gensokyo.events.ScarletDevilMansion;
import Gensokyo.events.TheEnmasDilemma;
import Gensokyo.events.ThoseEarthRabbits;
import Gensokyo.monsters.Kokoro;
import Gensokyo.monsters.Komachi;
import Gensokyo.monsters.Reimu;
import Gensokyo.monsters.YinYangOrb;
import Gensokyo.monsters.Yukari;
import Gensokyo.relics.CelestialsFlawlessClothing;
import Gensokyo.relics.Justice;
import Gensokyo.relics.LunaticRedEyes;
import Gensokyo.relics.Mercy;
import Gensokyo.relics.NagashiBinaDoll;
import Gensokyo.relics.OccultBall;
import Gensokyo.relics.PerfectCherryBlossom;
import Gensokyo.relics.PortableGap;
import Gensokyo.relics.YoukaiFlower;
import Gensokyo.savefields.BreadCrumbs;
import Gensokyo.savefields.ElitesSlain;
import Gensokyo.util.IDCheckDontTouchPls;
import Gensokyo.util.TextureLoader;
import Gensokyo.variables.DefaultCustomVariable;
import Gensokyo.variables.DefaultSecondMagicNumber;
import basemod.BaseMod;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.AddAudioSubscriber;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostDeathSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@SpireInitializer
public class GensokyoMod implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        PostInitializeSubscriber,
        AddAudioSubscriber {
    // Make sure to implement the subscribers *you* are using (read basemod wiki). Editing cards? EditCardsSubscriber.
    // Making relics? EditRelicsSubscriber. etc., etc., for a full list and how to make your own, visit the basemod wiki.
    public static final Logger logger = LogManager.getLogger(GensokyoMod.class.getName());
    private static String modID;

    //This is for the in-game mod settings panel.
    private static final String MODNAME = "Gensokyo";
    private static final String AUTHOR = "Darkglade"; // And pretty soon - You!
    private static final String DESCRIPTION = "A content mod inspired by Touhou Project.";
    
    // =============== INPUT TEXTURE LOCATION =================
    
    //Mod Badge - A small icon that appears in the mod settings menu next to your mod.
    public static final String BADGE_IMAGE = "GensokyoResources/images/Badge.png";
    
    // =============== MAKE IMAGE PATHS =================
    
    public static String makeCardPath(String resourcePath) {
        return getModID() + "Resources/images/cards/" + resourcePath;
    }
    
    public static String makeRelicPath(String resourcePath) {
        return getModID() + "Resources/images/relics/" + resourcePath;
    }
    
    public static String makeRelicOutlinePath(String resourcePath) {
        return getModID() + "Resources/images/relics/outline/" + resourcePath;
    }
    
    public static String makePowerPath(String resourcePath) {
        return getModID() + "Resources/images/powers/" + resourcePath;
    }
    
    public static String makeEventPath(String resourcePath) {
        return getModID() + "Resources/images/events/" + resourcePath;
    }

    public static String makeEffectPath(String resourcePath) {
        return getModID() + "Resources/images/effects/" + resourcePath;
    }
    
    // =============== /MAKE IMAGE PATHS/ =================
    
    // =============== /INPUT TEXTURE LOCATION/ =================
    
    
    // =============== SUBSCRIBE, CREATE THE COLOR_GRAY, INITIALIZE =================
    
    public GensokyoMod() {
        logger.info("Subscribe to BaseMod hooks");
        
        BaseMod.subscribe(this);
        
      /*
           (   ( /(  (     ( /( (            (  `   ( /( )\ )    )\ ))\ )
           )\  )\()) )\    )\()))\ )   (     )\))(  )\()|()/(   (()/(()/(
         (((_)((_)((((_)( ((_)\(()/(   )\   ((_)()\((_)\ /(_))   /(_))(_))
         )\___ _((_)\ _ )\ _((_)/(_))_((_)  (_()((_) ((_|_))_  _(_))(_))_
        ((/ __| || (_)_\(_) \| |/ __| __| |  \/  |/ _ \|   \  |_ _||   (_)
         | (__| __ |/ _ \ | .` | (_ | _|  | |\/| | (_) | |) |  | | | |) |
          \___|_||_/_/ \_\|_|\_|\___|___| |_|  |_|\___/|___/  |___||___(_)
      */
      
        setModID("Gensokyo");
        // cool
        // TODO: NOW READ THIS!!!!!!!!!!!!!!!:
        
        // 1. Go to your resources folder in the project panel, and refactor> rename GensokyoResources to
        // yourModIDResources.
        
        // 2. Click on the localization > eng folder and press ctrl+shift+r, then select "Directory" (rather than in Project)
        // replace all instances of Gensokyo with yourModID.
        // Because your mod ID isn't the default. Your cards (and everything else) should have Your mod ID. Not mine.
        
        // 3. FINALLY and most importantly: Scroll up a bit. You may have noticed the image locations above don't use getModID()
        // Change their locations to reflect your actual ID rather than Gensokyo. They get loaded before getID is a thing.
        
        logger.info("Done subscribing");
        
    }
    
    // ====== NO EDIT AREA ======
    // DON'T TOUCH THIS STUFF. IT IS HERE FOR STANDARDIZATION BETWEEN MODS AND TO ENSURE GOOD CODE PRACTICES.
    // IF YOU MODIFY THIS I WILL HUNT YOU DOWN AND DOWNVOTE YOUR MOD ON WORKSHOP
    
    public static void setModID(String ID) { // DON'T EDIT
        Gson coolG = new Gson(); // EY DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i hate u Gdx.files
        InputStream in = GensokyoMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THIS ETHER
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // OR THIS, DON'T EDIT IT
        logger.info("You are attempting to set your mod ID as: " + ID); // NO WHY
        if (ID.equals(EXCEPTION_STRINGS.DEFAULTID)) { // DO *NOT* CHANGE THIS ESPECIALLY, TO EDIT YOUR MOD ID, SCROLL UP JUST A LITTLE, IT'S JUST ABOVE
            throw new RuntimeException(EXCEPTION_STRINGS.EXCEPTION); // THIS ALSO DON'T EDIT
        } else if (ID.equals(EXCEPTION_STRINGS.DEVID)) { // NO
            modID = EXCEPTION_STRINGS.DEFAULTID; // DON'T
        } else { // NO EDIT AREA
            modID = ID; // DON'T WRITE OR CHANGE THINGS HERE NOT EVEN A LITTLE
        } // NO
        logger.info("Success! ID is " + modID); // WHY WOULD U WANT IT NOT TO LOG?? DON'T EDIT THIS.
    } // NO
    
    public static String getModID() { // NO
        return modID; // DOUBLE NO
    } // NU-UH
    
    private static void pathCheck() { // ALSO NO
        Gson coolG = new Gson(); // NNOPE DON'T EDIT THIS
        //   String IDjson = Gdx.files.internal("IDCheckStringsDONT-EDIT-AT-ALL.json").readString(String.valueOf(StandardCharsets.UTF_8)); // i still hate u btw Gdx.files
        InputStream in = GensokyoMod.class.getResourceAsStream("/IDCheckStringsDONT-EDIT-AT-ALL.json"); // DON'T EDIT THISSSSS
        IDCheckDontTouchPls EXCEPTION_STRINGS = coolG.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), IDCheckDontTouchPls.class); // NAH, NO EDIT
        String packageName = GensokyoMod.class.getPackage().getName(); // STILL NO EDIT ZONE
        FileHandle resourcePathExists = Gdx.files.internal(getModID() + "Resources"); // PLEASE DON'T EDIT THINGS HERE, THANKS
        if (!modID.equals(EXCEPTION_STRINGS.DEVID)) { // LEAVE THIS EDIT-LESS
            if (!packageName.equals(getModID())) { // NOT HERE ETHER
                throw new RuntimeException(EXCEPTION_STRINGS.PACKAGE_EXCEPTION + getModID()); // THIS IS A NO-NO
            } // WHY WOULD U EDIT THIS
            if (!resourcePathExists.exists()) { // DON'T CHANGE THIS
                throw new RuntimeException(EXCEPTION_STRINGS.RESOURCE_FOLDER_EXCEPTION + getModID() + "Resources"); // NOT THIS
            }// NO
        }// NO
    }// NO
    
    // ====== YOU CAN EDIT AGAIN ======
    
    
    @SuppressWarnings("unused")
    public static void initialize() {
        GensokyoMod gensokyoMod = new GensokyoMod();
        BreadCrumbs.initialize();
        ElitesSlain.initialize();
    }
    
    
    // =============== POST-INITIALIZE =================
    
    @Override
    public void receivePostInitialize() {
        logger.info("Loading badge image and mod options");
        
        // Load the Mod Badge
        Texture badgeTexture = TextureLoader.getTexture(BADGE_IMAGE);
        
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        
        BaseMod.registerModBadge(badgeTexture, MODNAME, AUTHOR, DESCRIPTION, settingsPanel);

        BaseMod.addMonster(Yukari.ID, (BaseMod.GetMonster)Yukari::new);
        BaseMod.addMonster(Kokoro.ID, (BaseMod.GetMonster)Kokoro::new);
        BaseMod.addMonster(Reimu.ID, (BaseMod.GetMonster)Reimu::new);
        BaseMod.addBoss(Gensokyo.ID, Yukari.ID, "GensokyoResources/images/monsters/Yukari/Yukari.png", "GensokyoResources/images/monsters/Yukari/YukariOutline.png");
        BaseMod.addBoss(Gensokyo.ID, Kokoro.ID, "GensokyoResources/images/monsters/Kokoro/Kokoro.png", "GensokyoResources/images/monsters/Kokoro/KokoroOutline.png");
        BaseMod.addBoss(Gensokyo.ID, Reimu.ID, "GensokyoResources/images/monsters/Reimu/Reimu.png", "GensokyoResources/images/monsters/Reimu/ReimuOutline.png");

        
        // =============== EVENTS =================

        BaseMod.addEvent(ScarletDevilMansion.ID, ScarletDevilMansion.class, Gensokyo.ID);
        BaseMod.addEvent(BorderOfDeath.ID, BorderOfDeath.class, Gensokyo.ID);
        BaseMod.addEvent(TheEnmasDilemma.ID, TheEnmasDilemma.class, Gensokyo.ID);
        BaseMod.addEvent(ACelestialsPlight.ID, ACelestialsPlight.class, Gensokyo.ID);
        BaseMod.addEvent(HakureiShrine.ID, HakureiShrine.class, Gensokyo.ID);
        BaseMod.addEvent(ASwiftSlash.ID, ASwiftSlash.class, Gensokyo.ID);
        BaseMod.addEvent(ThoseEarthRabbits.ID, ThoseEarthRabbits.class, Gensokyo.ID);
        BaseMod.addEvent(FieldTripToAnotherWorld.ID, FieldTripToAnotherWorld.class, Gensokyo.ID);
        BaseMod.addEvent(AHoleInReality.ID, AHoleInReality.class, Gensokyo.ID);
        BaseMod.addEvent(GoodsFromTheOutsideWorld.ID, GoodsFromTheOutsideWorld.class, Gensokyo.ID);
        BaseMod.addEvent(ForestOfMagic.ID, ForestOfMagic.class, Gensokyo.ID);
        BaseMod.addEvent(GoddessOfMisfortune.ID, GoddessOfMisfortune.class, Gensokyo.ID);
        BaseMod.addEvent(ClashOfLegends.ID, ClashOfLegends.class, Gensokyo.ID);
        BaseMod.addEvent(ALandWhereOnlyIAmMissing.ID, ALandWhereOnlyIAmMissing.class, Gensokyo.ID);
        BaseMod.addEvent(BambooForestOfTheLost.ID, BambooForestOfTheLost.class, Gensokyo.ID);
        BaseMod.addEvent(GardenOfTheSun.ID, GardenOfTheSun.class, Gensokyo.ID);
        
        // =============== /EVENTS/ =================
        Gensokyo.addAct(Exordium.ID, new Gensokyo());
        logger.info("Done loading badge Image and mod options");
    }
    
    // =============== / POST-INITIALIZE/ =================
    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio("Gensokyo:Train", makeEffectPath("TrainSFX.ogg"));
    }
    
    // ================ ADD RELICS ===================
    
    @Override
    public void receiveEditRelics() {
        logger.info("Adding relics");

        BaseMod.addRelic(new PerfectCherryBlossom(), RelicType.SHARED);
        BaseMod.addRelic(new Mercy(), RelicType.SHARED);
        BaseMod.addRelic(new Justice(), RelicType.SHARED);
        BaseMod.addRelic(new CelestialsFlawlessClothing(), RelicType.SHARED);
        BaseMod.addRelic(new OccultBall(), RelicType.SHARED);
        BaseMod.addRelic(new LunaticRedEyes(), RelicType.SHARED);
        BaseMod.addRelic(new PortableGap(), RelicType.SHARED);
        BaseMod.addRelic(new NagashiBinaDoll(), RelicType.SHARED);
        BaseMod.addRelic(new YoukaiFlower(), RelicType.SHARED);

        logger.info("Done adding relics!");
    }
    
    // ================ /ADD RELICS/ ===================
    
    
    // ================ ADD CARDS ===================
    
    @Override
    public void receiveEditCards() {
        logger.info("Adding variables");
        pathCheck();
        logger.info("Added variables");

        BaseMod.addDynamicVariable(new DefaultCustomVariable());
        BaseMod.addDynamicVariable(new DefaultSecondMagicNumber());

        BaseMod.addCard(new CrescentMoonSlash());

        //Urban Legends
        BaseMod.addCard(new MissMary());
        BaseMod.addCard(new SpontaneousHumanCombustion());
        BaseMod.addCard(new RedCapeBlueCape());
        BaseMod.addCard(new TekeTeke());
        BaseMod.addCard(new Kunekune());
        BaseMod.addCard(new HAARP());
        BaseMod.addCard(new MenInBlack());
        BaseMod.addCard(new LittleGreenMen());
        BaseMod.addCard(new TurboGranny());
        BaseMod.addCard(new MonkeysPaw());
        BaseMod.addCard(new ManorOfTheDishes());
        BaseMod.addCard(new LochNessMonster());
        BaseMod.addCard(new GapWoman());
        BaseMod.addCard(new EightFeetTall());
        BaseMod.addCard(new SevenSchoolMysteries());
        BaseMod.addCard(new Apocalypse());
        BaseMod.addCard(new SlitMouthedWoman());
    }
    
    // ================ /ADD CARDS/ ===================
    
    
    // ================ LOAD THE TEXT ===================
    
    @Override
    public void receiveEditStrings() {
        logger.info("You seeing this?");
        logger.info("Beginning to edit strings for mod with ID: " + getModID());
        
        // CardStrings
        BaseMod.loadCustomStringsFile(CardStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Card-Strings.json");
        
        // PowerStrings
        BaseMod.loadCustomStringsFile(PowerStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Power-Strings.json");
        
        // RelicStrings
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Relic-Strings.json");
        
        // Event Strings
        BaseMod.loadCustomStringsFile(EventStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Event-Strings.json");

        //Monster Strings
        BaseMod.loadCustomStringsFile(MonsterStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-Monster-Strings.json");

        BaseMod.loadCustomStringsFile(UIStrings.class,
                getModID() + "Resources/localization/eng/GensokyoMod-ui.json");
        
        logger.info("Done edittting strings");
    }
    
    // ================ /LOAD THE TEXT/ ===================
    
    // ================ LOAD THE KEYWORDS ===================
    
    @Override
    public void receiveEditKeywords() {
        // Keywords on cards are supposed to be Capitalized, while in Keyword-String.json they're lowercase
        //
        // Multiword keywords on cards are done With_Underscores
        //
        // If you're using multiword keywords, the first element in your NAMES array in your keywords-strings.json has to be the same as the PROPER_NAME.
        // That is, in Card-Strings.json you would have #yA_Long_Keyword (#y highlights the keyword in yellow).
        // In Keyword-Strings.json you would have PROPER_NAME as A Long Keyword and the first element in NAMES be a long keyword, and the second element be a_long_keyword
        
        Gson gson = new Gson();
        String json = Gdx.files.internal(getModID() + "Resources/localization/eng/GensokyoMod-Keyword-Strings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        
        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(getModID().toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                //  getModID().toLowerCase() makes your keyword mod specific (it won't show up in other cards that use that word)
            }
        }
    }
    
    // ================ /LOAD THE KEYWORDS/ ===================    
    
    // this adds "ModName:" before the ID of any card/relic/power etc.
    // in order to avoid conflicts if any other mod uses the same ID.
    public static String makeID(String idText) {
        return getModID() + ":" + idText;
    }
}
