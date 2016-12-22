package com.ecs160.nittacraft;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecs160.fbs.Command;
import com.ecs160.fbs.Game;
import com.ecs160.fbs.PlayerStatus;
import com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType;
import com.ecs160.nittacraft.CGameDataTypes.EPlayerColor;
import com.ecs160.nittacraft.CGameModel.SGameEvent;
import com.ecs160.nittacraft.activities.BaseActivity;
import com.ecs160.nittacraft.capabilities.CPlayerCapability;
import com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType;
import com.ecs160.nittacraft.maps.CAssetDecoratedMap;
import com.ecs160.nittacraft.maps.CGraphicRecolorMap;
import com.ecs160.nittacraft.renderers.CAssetRenderer;
import com.ecs160.nittacraft.renderers.CFogRenderer;
import com.ecs160.nittacraft.renderers.CMapRenderer;
import com.ecs160.nittacraft.renderers.CMiniMapRenderer;
import com.ecs160.nittacraft.renderers.CResourceRenderer;
import com.ecs160.nittacraft.renderers.CUnitActionRenderer;
import com.ecs160.nittacraft.renderers.CUnitDescriptionRenderer;
import com.ecs160.nittacraft.renderers.CViewportRenderer;
import com.ecs160.nittacraft.views.GameView;
import com.ecs160.nittacraft.views.SideBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.ecs160.nittacraft.CApplicationData.ECursorType.ctMax;
import static com.ecs160.nittacraft.CApplicationData.ECursorType.ctPointer;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmBattle;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmGameMenu;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmGameOver;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmInvalidUser;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmLANHostInfo;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmLocalMultiPlayerMenu;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmMainMenu;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmMapSelect;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmMultiPlayerLobby;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmMultiPlayerOptionsMenu;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmOnlineMultiPlayerMenu;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmOptionsMenu;
import static com.ecs160.nittacraft.CApplicationData.EGameMode.gmPlayerAISelect;
import static com.ecs160.nittacraft.CApplicationData.EGameSessionType.gstMultiPlayerClient;
import static com.ecs160.nittacraft.CApplicationData.EGameSessionType.gstMultiPlayerHost;
import static com.ecs160.nittacraft.CApplicationData.EGameSessionType.gstSinglePlayer;
import static com.ecs160.nittacraft.CApplicationData.EInputState.isNone;
import static com.ecs160.nittacraft.CApplicationData.EInputState.isTarget;
import static com.ecs160.nittacraft.CApplicationData.EPlayerType.ptAIEasy;
import static com.ecs160.nittacraft.CApplicationData.EPlayerType.ptHuman;
import static com.ecs160.nittacraft.CApplicationData.EPlayerType.ptNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actAttack;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actBuildPeasant;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actConvey;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actMove;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetCapabilityType.actRepair;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atArcher;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atBarracks;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atBlacksmith;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atCannonTower;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atCastle;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atFarm;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atFootman;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGold;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldMine;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGoldVein;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atGuardTower;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atKeep;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atKnight;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atLumber;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atLumberMill;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atNone;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atPeasant;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atRanger;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atScoutTower;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atTownHall;
import static com.ecs160.nittacraft.CGameDataTypes.EAssetType.atWall;
import static com.ecs160.nittacraft.CGameDataTypes.EPlayerColor.pcBlue;
import static com.ecs160.nittacraft.CGameDataTypes.EPlayerColor.pcMax;
import static com.ecs160.nittacraft.CGameDataTypes.EPlayerColor.pcNone;
import static com.ecs160.nittacraft.CGameModel.EEventType.etButtonTick;
import static com.ecs160.nittacraft.CGameModel.EEventType.etPlaceAction;
import static com.ecs160.nittacraft.CGameModel.EEventType.etSelection;
import static com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType.ttAsset;
import static com.ecs160.nittacraft.capabilities.CPlayerCapability.ETargetType.ttTerrainOrAsset;
import static com.ecs160.nittacraft.maps.CAssetDecoratedMap.DuplicateMap;
import static com.ecs160.nittacraft.maps.CTerrainMap.ETileType.ttTree;
import static com.ecs160.nittacraft.views.GameView.DApplicationData;

public class CApplicationData {
    public static final int TIMEOUT_INTERVAL = 50;

    public enum EGameMode {
        gmMainMenu,
        gmOptionsMenu,
        gmSoundOptions,
        gmNetworkOptions,
        gmOnlineMultiPlayerMenu,
        gmLocalMultiPlayerMenu,
        gmMultiPlayerOptionsMenu,
        gmLANHostInfo,
        gmMapSelect,
        gmHostSelect,
        gmMultiPlayerLobby,
        gmPlayerAISelect,
        gmBattle,
        gmGameMenu,
        gmGameOptions,
        gmGameOver,
        gmInvalidUser,
        gmNoServerResponse
    }

    enum ECursorType {
        ctPointer,
        ctInspect,
        ctArrowN,
        ctArrowE,
        ctArrowS,
        ctArrowW,
        ctTargetOff,
        ctTargetOn,
        ctMax
    }

    enum EUIComponentType {
        uictNone,
        uictViewport,
        uictViewportBevelN,
        uictViewportBevelE,
        uictViewportBevelS,
        uictViewportBevelW,
        uictMiniMap,
        uictUserDescription,
        uictUserAction,
        uictMenuButton
    }

    enum EGameSessionType {
        gstSinglePlayer,
        gstMultiPlayerHost,
        gstMultiPlayerClient
    }

    enum EPlayerType {
        ptNone,
        ptHuman,
        ptAIEasy,
        ptAIMedium,
        ptAIHard
    }

    public enum EInputState {
        isNone,
        isSelected,
        isCommand,
        isTarget
    }

    private ViewGroup DMapContainer, DSidebarContainer;
    private GameView DGameView;
    private SideBarView DSideBarView;
    public static BaseActivity DMainActivity;
    public static Typeface DTypeface;

    public void InitializeViews(ViewGroup map, ViewGroup sidebar, BaseActivity mainActivity) {
        DMapContainer = map;
        DSidebarContainer = sidebar;
        DMainActivity = mainActivity;
    }

    public void InitializeMapView() {
        DGameView = GameView.getInstance(DMapContainer.getContext());
    }

    public void InitializeSideView() {
        DSideBarView = SideBarView.getInstance(DSidebarContainer.getContext());
    }

    private boolean DInGameFlag = false;
    private boolean DSetNetworkInfo = false;
    private boolean GameOvered = false;
    private boolean DLoggedIn = false;
    private boolean DReady = false;
    private boolean DPlayerStatus;
    private boolean DOnlineMultiPlayer = false;
    private boolean DDeleted;
    private EGameSessionType DGameSessionType;
    private float DSoundVolume;
    private float DMusicVolume;
    private String DUsername;
    private String DPassword;
    private String DRemoteHostname;
    private int DMultiplayerPort;
    private CPeriodicTimeout DPeriodicTimeout;
//    private GtkWidget DMainWindow;
//    private GtkWidget DDrawingArea;
//    private GdkCursor DBlankCursor;
//    private GdkGC DDrawingContext;
//    private GdkPixmap DDoubleBufferPixmap;
//    private GdkPixmap DWorkingBufferPixmap;
//    private GdkPixbuf DWorkingPixbuf;
    private Canvas DMiniMapPixmap;
    private Canvas DViewportPixmap;
//    private GdkPixmap DViewportTypePixmap;
//    private GdkPixmap DUnitDescriptionPixmap;
//    private GdkPixmap DUnitActionPixmap;
//    private GdkPixmap DResourcePixmap;
//    private GdkPixmap DMapSelectListViewPixmap;
//    private GdkColor DMiniMapViewportColor;
    private int DBorderWidth;
    private int DPanningSpeed;
    private int DViewportXOffset, DViewportYOffset;
    private int DMiniMapXOffset, DMiniMapYOffset;
    private int DUnitDescriptionXOffset, DUnitDescriptionYOffset;
    private int DUnitActionXOffset, DUnitActionYOffset;
    private int DMenuButtonXOffset, DMenuButtonYOffset;
    private int DMapSelectListViewXOffset, DMapSelectListViewYOffset;
    private int DLobbySelectListViewXOffset, DLobbySelectListViewYOffset;
    private String DMainMenuTitle;
    private ArrayList<String> DMainMenuButtonsText;
//    private ArrayList<TButtonCallbackFunction> DMainMenuButtonsFunctions;
    private String DOptionsMenuTitle;
    private ArrayList<String> DOptionsMenuButtonsText;
//    private ArrayList<TButtonCallbackFunction> DOptionsMenuButtonsFunctions;
    private String DMultiPlayerOptionsMenuTitle;
    private ArrayList<String> DMultiPlayerOptionsMenuButtonsText;
//    private ArrayList<TButtonCallbackFunction> DMultiPlayerOptionsMenuButtonsFunctions;
    private String DGameMenuTitle;
    private ArrayList<String> DGameMenuButtonsText;
//    private ArrayList<TButtonCallbackFunction> DGameMenuButtonsFunctions;
    private String DCurrentPageTitle;
    private ArrayList<String> DCurrentPageButtonsText;
//    private ArrayList<TButtonCallbackFunction> DCurrentPageButtonsFunctions;
//    private ArrayList<SRectangle> DCurrentPageButtonLocations;
//    private ArrayList<SRectangle> DColorButtonLocations;
//    private ArrayList<SRectangle> DPlayerTypeButtonLocations;
    private CGameDataTypes.EPlayerColor DPlayerColorRequestingChange;
    private CGameDataTypes.EPlayerColor DPlayerColorChangeRequest;
    private CGameDataTypes.EPlayerColor DPlayerColorRequesTypeChange;
    private String DPlayerNameRequestTypeChange = "AIRed";
    private boolean DCurrentPageButtonHovered;
    private int DSelectedMapOffset;
    private int DSelectedMapIndex;
    private CAssetDecoratedMap DSelectedMap;

    private String DSelectedGameName;
    private int DSelectedGameOffset;
    private int DSelectedGameIndex;
    private ArrayList<String> DLobbyNames;
    private int DOptionsEditSelected;
    private int DOptionsEditSelectedCharacter;
    private Context DContext;
    private ArrayList<SRectangle> DOptionsEditLocations;
//    private ArrayList<SRectangle> DOptionsEditLocations;
    private ArrayList<String> DOptionsEditTitles;
    private ArrayList<String> DOptionsEditText;
//    private ArrayList<TEditTextValidationCallbackFunction> DOptionsEditValidationFunctions;

    private CMapRenderer DMapRenderer;
//    private CCursorSet DCursorSet;
    private int[] DCursorIndices = new int[ctMax.ordinal()];
    private ECursorType DCursorType;
    private CSoundLibraryMixer DSoundLibraryMixer;
    private CSoundEventRenderer DSoundEventRenderer;
//    private CFontTileset DFonts[CUnitDescriptionRenderer::fsMax];
    private CGraphicTileset DSplashTileset;
    public CGraphicTileset DMarkerTileset;
    public CGraphicTileset DButtonColorTileset;
    private CGraphicTileset DBackgroundTileset;
    private CGraphicTileset DMiniBevelTileset;
    private CGraphicTileset DInnerBevelTileset;
    private CGraphicTileset DOuterBevelTileset;
    private CGraphicTileset DListViewIconTileset;
//    private CBevel DMiniBevel;
//    private CBevel DInnerBevel;
//    private CBevel DOuterBevel;
    public CGraphicTileset DTerrainTileset;
    public CGraphicTileset DFogTileset;
    public CGraphicRecolorMap DRecolorMap;
    public static CGraphicMulticolorTileset DIconTileset;
    private CGraphicTileset DMiniIconTileset;
    public ArrayList<CGraphicMulticolorTileset> DAssetTilesets;
    public ArrayList<CGraphicTileset> DFireTilesets;
    public CGraphicTileset DBuildingDeathTileset;
    public CGraphicTileset DCorpseTileset;
    public CGraphicTileset DArrowTileset;
    private CAssetRenderer DAssetRenderer;
    private CFogRenderer DFogRenderer;
    private CViewportRenderer DViewportRenderer;
    private CMiniMapRenderer DMiniMapRenderer;
    private CMultiplayer DMultiplayer;
    private CUnitDescriptionRenderer DUnitDescriptionRenderer;
    public CUnitActionRenderer DUnitActionRenderer;
    private CResourceRenderer DResourceRenderer;
//    private CButtonRenderer DMenuButtonRenderer;
//    private CButtonRenderer DCurrentPageButtonRenderer;
//    private CListViewRenderer DMapSelectListViewRenderer;
//    private CEditRenderer DOptionsEditRenderer;

    // Model
    public CGameDataTypes.EPlayerColor DPlayerColor;
    public CGameModel DGameModel;
    private SPlayerCommandRequest[] DPlayerCommands = new SPlayerCommandRequest[pcMax.ordinal()];
    private ArrayList<CAIPlayer> DAIPlayers = new ArrayList<>();
    private EPlayerType[] DLoadingPlayerTypes = new EPlayerType[pcMax.ordinal()];
    private CGameDataTypes.EPlayerColor[] DLoadingPlayerColors = new CGameDataTypes
            .EPlayerColor[pcMax.ordinal()];
    private boolean[] DLoadingPlayerStatus = new boolean[pcMax.ordinal()];
    private String[] DPlayerNames = new String[pcMax.ordinal()];

    private EGameMode DGameMode;
    private EGameMode DNextGameMode;

    public EAssetCapabilityType selectedCapability;

    private HashMap<Long, CGameDataTypes.EAssetCapabilityType> DUnitHotKeyMap;
    private HashMap<Long, CGameDataTypes.EAssetCapabilityType> DBuildHotKeyMap;
    private HashMap<Long, CGameDataTypes.EAssetCapabilityType> DTrainHotKeyMap;
//    private CPlayerAsset[][] DSelectedPlayerAssets = new CPlayerAsset[pcMax.ordinal()][];
    public ArrayList<ArrayList<CPlayerAsset>> DSelectedPlayerAssets;
    public CGameDataTypes.EAssetCapabilityType[] DCurrentAssetCapability = new
            CGameDataTypes.EAssetCapabilityType[pcMax.ordinal()];
    private ArrayList<Long>[] DPressedKeys = (ArrayList<Long>[]) new ArrayList[pcMax.ordinal()];
    private ArrayList<Long>[] DReleasedKeys = (ArrayList<Long>[]) new ArrayList[pcMax.ordinal()];
    private int[] DCurrentX = new int[pcMax.ordinal()];
    private int[] DCurrentY = new int[pcMax.ordinal()];
    private CPosition[] DMouseDown = new CPosition[pcMax.ordinal()];
    private int[] DLeftClick = new int[pcMax.ordinal()];
    private int[] DRightClick = new int[pcMax.ordinal()];
    private boolean[] DLeftDown = new boolean[pcMax.ordinal()];
    private boolean[] DRightDown = new boolean[pcMax.ordinal()];
    public static int DTileScaleFactor = 2;
    public static EInputState DInputState;
    public static int DMMRefresh = 0;
//    private CButtonRenderer.EButtonState DMenuButtonState;

    public Context DContext() {
        return DContext;
    }

    public CApplicationData() {

        DInputState = isNone;
        DGameMode = DNextGameMode = gmMainMenu;
        DPlayerColor = pcBlue;
//        DMiniMapViewportColor = {0xFFFFFF, 0xFFFF, 0xFFFF, 0xFFFF};
        DDeleted = false;
//        DMainWindow = null;
//        DDrawingArea = null;
//        DBlankCursor = null;
//        DDrawingContext = null;
//        DDoubleBufferPixmap = null;
//        DWorkingBufferPixmap = null;
        DViewportPixmap = null;
//        DViewportTypePixmap = null;
//        DWorkingPixbuf = null;
//        DMiniMapPixmap = null;
//        DUnitDescriptionPixmap = null;
//        DUnitActionPixmap = null;
//        DResourcePixmap = null;
//        DMapSelectListViewPixmap = null;
        DCursorType = ctPointer;

        DMapSelectListViewXOffset = 0;
        DMapSelectListViewYOffset = 0;
        DLobbySelectListViewXOffset = 0;
        DLobbySelectListViewYOffset = 0;
        DSelectedMapOffset = 0;
        DSelectedMapIndex = 0;
        DSelectedGameName = "";
        DSelectedGameOffset = 0;
        DSelectedGameIndex = 0;

        DSoundVolume = 1.0f;
        DMusicVolume = 0.5f;
        DUsername = "charles";
        DPassword = "charles123";
        DRemoteHostname = "localhost";
        DMultiplayerPort = 55107;
        DMultiplayer = new CMultiplayer();

        DBorderWidth = 32;
        DPanningSpeed = 0;
        DSelectedPlayerAssets = new ArrayList<>();

        for (int i = 0; i < pcMax.ordinal() + 1; i++)
            DSelectedPlayerAssets.add(i, null);

        for (int Index = 0; Index < pcMax.ordinal(); Index++) {
            DCurrentX[Index] = 0;
            DCurrentY[Index] = 0;
            DMouseDown[Index] = new CPosition(-1, -1);
            DLeftClick[Index] = 0;
            DRightClick[Index] = 0;
            DLeftDown[Index] = false;
            DRightDown[Index] = false;
            DPlayerCommands[Index] = new SPlayerCommandRequest();
            DPlayerCommands[Index].DAction = actNone;
            DLoadingPlayerColors[Index] = CGameDataTypes.EPlayerColor.values()[Index];
            DSelectedPlayerAssets.add(Index, null);
            DAIPlayers.add(null);
        }

//        DMenuButtonState = CButtonRenderer.bsNone;
//        DUnitHotKeyMap[GDK_KEY_A] = actAttack;
//        DUnitHotKeyMap[GDK_KEY_a] = actAttack;
//        DUnitHotKeyMap[GDK_KEY_B] = actBuildSimple;
//        DUnitHotKeyMap[GDK_KEY_b] = actBuildSimple;
//        DUnitHotKeyMap[GDK_KEY_G] = actConvey;
//        DUnitHotKeyMap[GDK_KEY_g] = actConvey;
//        DUnitHotKeyMap[GDK_KEY_M] = actMove;
//        DUnitHotKeyMap[GDK_KEY_m] = actMove;
//        DUnitHotKeyMap[GDK_KEY_P] = actPatrol;
//        DUnitHotKeyMap[GDK_KEY_p] = actPatrol;
//        DUnitHotKeyMap[GDK_KEY_R] = actRepair;
//        DUnitHotKeyMap[GDK_KEY_r] = actRepair;
//        DUnitHotKeyMap[GDK_KEY_T] = actStandGround;
//        DUnitHotKeyMap[GDK_KEY_t] = actStandGround;
//
//        DBuildHotKeyMap[GDK_KEY_B] = actBuildBarracks;
//        DBuildHotKeyMap[GDK_KEY_b] = actBuildBarracks;
//        DBuildHotKeyMap[GDK_KEY_F] = actBuildFarm;
//        DBuildHotKeyMap[GDK_KEY_f] = actBuildFarm;
//        DBuildHotKeyMap[GDK_KEY_H] = actBuildTownHall;
//        DBuildHotKeyMap[GDK_KEY_h] = actBuildTownHall;
//        DBuildHotKeyMap[GDK_KEY_L] = actBuildLumberMill;
//        DBuildHotKeyMap[GDK_KEY_l] = actBuildLumberMill;
//        DBuildHotKeyMap[GDK_KEY_S] = actBuildBlacksmith;
//        DBuildHotKeyMap[GDK_KEY_s] = actBuildBlacksmith;
//        DBuildHotKeyMap[GDK_KEY_T] = actBuildScoutTower;
//        DBuildHotKeyMap[GDK_KEY_t] = actBuildScoutTower;
//
//        DTrainHotKeyMap[GDK_KEY_A] = actBuildArcher;
//        DTrainHotKeyMap[GDK_KEY_a] = actBuildArcher;
//        DTrainHotKeyMap[GDK_KEY_F] = actBuildFootman;
//        DTrainHotKeyMap[GDK_KEY_f] = actBuildFootman;
//        DTrainHotKeyMap[GDK_KEY_P] = actBuildPeasant;
//        DTrainHotKeyMap[GDK_KEY_p] = actBuildPeasant;
    }

    private boolean LoadDatFiles(Context context) {
        CDataSource TempDataSource;

        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.sound_clips));
        DSoundLibraryMixer = new CSoundLibraryMixer();

        if (!DSoundLibraryMixer.LoadLibrary(TempDataSource, context)) {
            Log.e("CApplicationData", "Failed to sound mixer.\n");
            return false;
        }
        DSoundLibraryMixer.PlaySong(DSoundLibraryMixer.FindSong("load"), DMusicVolume);

        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.button_colors));
        DButtonColorTileset = new CGraphicTileset(context);

        if (!DButtonColorTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load markers.\n");
            return false;
        }

        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.marker));
        DMarkerTileset = new CGraphicTileset(context);

        if (!DMarkerTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load markers.\n");
            return false;
        }
        
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.texture));
        DBackgroundTileset = new CGraphicTileset(context);

        if (!DBackgroundTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load background tileset.\n");
            return false;
        }


//        TempDataSource = TempDataContainer.DataSource("FontKingthings10.dat");
//        DFonts[CUnitDescriptionRenderer::fsSmall] = new CFontTileset();
//        if (!DFonts[CUnitDescriptionRenderer::fsSmall].LoadFont(TempDataSource)) {
//            Log.d("Error", "Failed to load font tileset.\n");
//            return false;
//        }
//        TempDataSource = TempDataContainer.DataSource("FontKingthings12.dat");
//        DFonts[CUnitDescriptionRenderer::fsMedium] = new CFontTileset();
//        if (!DFonts[CUnitDescriptionRenderer::fsMedium].LoadFont(TempDataSource)) {
//            Log.d("Error", "Failed to load font tileset.\n");
//            return false;
//        }
//        TempDataSource = TempDataContainer.DataSource("FontKingthings16.dat");
//        DFonts[CUnitDescriptionRenderer::fsLarge] = new CFontTileset();
//        if (!DFonts[CUnitDescriptionRenderer::fsLarge].LoadFont(TempDataSource)) {
//            Log.d("Error", "Failed to load font tileset.\n");
//            return false;
//        }
//        TempDataSource = TempDataContainer.DataSource("FontKingthings24.dat");
//        DFonts[CUnitDescriptionRenderer::fsGiant] = new CFontTileset();
//        if (!DFonts[CUnitDescriptionRenderer::fsGiant].LoadFont(TempDataSource)) {
//            Log.d("Error", "Failed to load font tileset.\n");
//            return false;
//        }

        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.mini_bevel));
        DMiniBevelTileset = new CGraphicTileset(context);
        if (!DMiniBevelTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load bevel tileset.\n");
            return false;
        }

        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.inner_bevel));
        DInnerBevelTileset = new CGraphicTileset(context);
        if (!DInnerBevelTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load bevel tileset.\n");
            return false;
        }

        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.outer_bevel));
        DOuterBevelTileset = new CGraphicTileset(context);
        if (!DOuterBevelTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load bevel tileset.\n");
            return false;
        }

        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.list_view_icons));
        DListViewIconTileset = new CGraphicTileset(context);
        if (!DListViewIconTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load list view tileset.\n");
            return false;
        }

        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.terrain));
        DTerrainTileset = new CGraphicTileset(context);
        if (!DTerrainTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load tileset.\n");
            return false;
        }

        //FIXME Unsure, but this needs to be scaled x2 for things to appear even close to correct.
        CPosition.SetTileDimensions(DTerrainTileset.TileWidth(), DTerrainTileset.TileHeight());
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.fog));
        DFogTileset = new CGraphicTileset(context);
        if (!DFogTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load tileset.\n");
            return false;
        }

//        TempDataSource = TempDataContainer.DataSource("png/Colors.png");
        DRecolorMap = new CGraphicRecolorMap();
//        DAssetTilesets.ensureCapacity(atMax.ordinal());
        DAssetTilesets = new ArrayList<>();
        DFireTilesets = new ArrayList<>();

        Log.d("DEBUG_LOW",  "Loading Icons\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.icons));
        DIconTileset = new CGraphicMulticolorTileset(context);
        if (!DIconTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load icons.\n");
            return false;
        }

        Log.d("DEBUG_LOW",  "Loading Mini Icons\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.mini_icons));
        DMiniIconTileset = new CGraphicTileset(context);
        if (!DMiniIconTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load mini icons.\n");
            return false;
        }

        Log.d("DEBUG_LOW",  "Loading Coprse\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.corpse));
        DCorpseTileset = new CGraphicTileset(context);
        if (!DCorpseTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load corpse tileset.\n");
            return false;
        }

        Log.d("DEBUG_LOW",  "Loading FireSmall\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.fire_small));
        CGraphicTileset FireTileset = new CGraphicTileset(context);
        if (!FireTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load fire small tileset.\n");
            return false;
        }
        DFireTilesets.add(FireTileset);

        Log.d("DEBUG_LOW",  "Loading FireLarge\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.fire_large));
        FireTileset = new CGraphicTileset(context);
        if (!FireTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load fire large tileset.\n");
            return false;
        }
        DFireTilesets.add(FireTileset);

        Log.d("DEBUG_LOW",  "Loading BuildingDeath\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.building_death));
        DBuildingDeathTileset = new CGraphicTileset(context);
        if (!DBuildingDeathTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load building death tileset.\n");
            return false;
        }

        Log.d("DEBUG_LOW",  "Loading Arrow\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.arrow));
        DArrowTileset = new CGraphicTileset(context);
        if (!DArrowTileset.LoadTileset(TempDataSource)) {
            Log.d("Error", "Failed to load arrow tileset.\n");
            return false;
        }

        CGraphicMulticolorTileset TempTileset;
        
        Log.d("DEBUG_LOW",  "Loading AssetNone\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.asset_none));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load character tileset.\n");
            return false;
        }
        DAssetTilesets.add(atNone.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Peasant\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.peasant));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load peasant tileset.\n");
            return false;
        }
        DAssetTilesets.add(atPeasant.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Footman\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.footman));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load footman tileset.\n");
            return false;
        }
        DAssetTilesets.add(atFootman.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Archer\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.archer));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load archer tileset.\n");
            return false;
        }
        DAssetTilesets.add(atArcher.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Ranger\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.ranger));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load archer tileset.\n");
            return false;
        }
        DAssetTilesets.add(atRanger.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading GoldMine\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.gold_mine));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load gold mine tileset.\n");
            return false;
        }
        DAssetTilesets.add(atGoldMine.ordinal(), TempTileset);

        Log.d("DEBUG_LOW", "Loading GoldVein\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.goldvein));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load gold vein tileset.\n");
            return false;
        }
        DAssetTilesets.add(atGoldVein.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading TownHall\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.town_hall));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load town hall tileset.\n");
            return false;
        }
        DAssetTilesets.add(atTownHall.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Keep\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.keep));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load keep tileset.\n");
            return false;
        }
        DAssetTilesets.add(atKeep.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Castle\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.castle));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load castle tileset.\n");
            return false;
        }
        DAssetTilesets.add(atCastle.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Farm\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.farm));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load farm tileset.\n");
            return false;
        }
        DAssetTilesets.add(atFarm.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Barracks\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.barracks));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load barracks tileset.\n");
            return false;
        }
        DAssetTilesets.add(atBarracks.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading LumberMill\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.lumber_mill));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load lumber mill tileset.\n");
            return false;
        }
        DAssetTilesets.add(atLumberMill.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Blacksmith\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.blacksmith));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load blacksmith tileset.\n");
            return false;
        }
        DAssetTilesets.add(atBlacksmith.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading ScoutTower\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.scout_tower));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load scout tower tileset.\n");
            return false;
        }
        DAssetTilesets.add(atScoutTower.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading GuardTower\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.guard_tower));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load guard tower tileset.\n");
            return false;
        }
        DAssetTilesets.add(atGuardTower.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading CannonTower\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.cannon_tower));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.d("Error", "Failed to load cannon tower tileset.\n");
            return false;
        }
        DAssetTilesets.add(atCannonTower.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Gold\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.gold));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.e("Error", "Failed to load gold tileset.\n");
            return false;
        }
        DAssetTilesets.add(atGold.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Lumber\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.lumber));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.e("Error", "Failed to load lumber tileset.\n");
            return false;
        }
        DAssetTilesets.add(atLumber.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Wall\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.wall));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.e("Error", "Failed to load wall tileset.\n");
            return false;
        }
        DAssetTilesets.add(atWall.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Loading Knight\n");
        TempDataSource = new CDataSource(context.getResources().openRawResource(R.raw.knight));
        TempTileset = new CGraphicMulticolorTileset(context);
        if (!TempTileset.LoadTileset(DRecolorMap, TempDataSource)) {
            Log.e("Error", "Failed to load knight tileset.\n");
            return false;
        }
        DAssetTilesets.add(atKnight.ordinal(), TempTileset);

        Log.d("DEBUG_LOW",  "Assets Loaded\n");

        Log.d("DEBUG_LOW",  "Loading res directory\n");
        if (!CPlayerAssetType.LoadTypes(context)) {
            Log.d("Error", "Failed to load resources\n");
            return true;
        }


        Log.d("DEBUG_LOW",  "Loading upg directory\n");
        if (!CPlayerUpgrade.LoadUpgrades(context)) {
            Log.d("Error", "Failed to load upgrades\n");
            return true;
        }


        Log.d("DEBUG_LOW",  "Loading Maps\n");
        AssetManager AstMngr = context.getAssets();
        if (!CAssetDecoratedMap.LoadMaps(AstMngr)) {
            Log.e("CApplicationData", "Failed to load maps.");
            return true;
        }

        return true;
    }

    public int Init(Context context) {

        DContext = context;

        // Load dat files
        if (!LoadDatFiles(context)) {
            return -1;
        }

        DPeriodicTimeout = new CPeriodicTimeout(TIMEOUT_INTERVAL);
        CPlayerAsset.UpdateFrequency(DPeriodicTimeout.Frequency());
        CAssetRenderer.UpdateFrequency(DPeriodicTimeout.Frequency());

        // TODO: Is this call needed?
//        LoadGameMap(0);

//        DCurrentPageButtonRenderer = new CButtonRenderer(DButtonColorTileset, DInnerBevel,
//                DOuterBevel, DFonts[CUnitDescriptionRenderer::fsLarge]);
//
//        DMapSelectListViewRenderer = new CListViewRenderer(DListViewIconTileset,
//                DFonts[CUnitDescriptionRenderer::fsLarge]);
//
//        DOptionsEditRenderer = new CEditRenderer(DButtonColorTileset, DInnerBevel,
//                DFonts[CUnitDescriptionRenderer::fsLarge]);

        //ResizeCanvases();
        ChangeMode(gmMainMenu);
//        SwitchButtonMenuData();
//        gdk_draw_pixmap(DDoubleBufferPixmap, DDrawingContext, DWorkingBufferPixmap, 0, 0, 0, 0, -1, -1);
//        gdk_draw_pixmap(DDrawingArea.window, DDrawingContext, DDoubleBufferPixmap, 0, 0, 0, 0, -1, -1);
//
//        // Initialize multiplayer objects.
//        DMultiplayer = new CMultiplayer();
//
        TimeoutCallback();
        DSoundLibraryMixer.StopSong();
        DSoundLibraryMixer.PlaySong(DSoundLibraryMixer.FindSong("menu"), DMusicVolume);
        return 0;
    }

    private boolean TimeoutCallback() {
        if (DDeleted) {
            return false;
        }

        if (Timeout()) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TimeoutCallback();
                }
            }, DPeriodicTimeout.MiliSecondsUntilDeadline());
        }
        return true;
    }

//    /**
//     * This is a callback function to signify that the delete windows has been called.
//     * If you return FALSE in the "delete-event" signal handler, GTK will emit the
//     * "destroy" signal. Returning TRUE means you don't want the window to be
//     * destroyed. This is useful for popping up 'are you sure you want to quit?'
//     * type dialogs.
//     **/
//    static boolean MainWindowDeleteEventCallback(GtkWidget*widget, GdkEvent*event, gpointer
//            data) {
//        CApplicationData AppData = new CApplicationData(data);
//
//        return AppData.MainWindowDeleteEvent(widget, event);
//    }
//
//    // The destroy window callback
//    static void MainWindowDestroyCallback(GtkWidget*widget, gpointer data) {
//        CApplicationData AppData = new CApplicationData(data);
//
//        AppData.MainWindowDestroy(widget);
//    }
//
//    boolean MainWindowKeyPressEventCallback(GtkWidget*widget, GdkEventKey*event, gpointer data) {
//        CApplicationData AppData = new CApplicationData(data);
//
//        return AppData.MainWindowKeyPressEvent(widget, event);
//    }
//
//    boolean MainWindowKeyReleaseEventCallback(GtkWidget*widget, GdkEventKey*event, gpointer data) {
//        CApplicationData AppData = new CApplicationData(data);
//
//        return AppData.MainWindowKeyReleaseEvent(widget, event);
//    }
//
//    boolean MainWindowConfigureEventCallback(GtkWidget*widget, GdkEvent*event, gpointer data) {
//        CApplicationData AppData = new CApplicationData(data);
//
//        return AppData.MainWindowConfigureEvent(widget, event);
//    }
//
//    boolean DrawingAreaExposeCallback(GtkWidget*widget, GdkEventExpose*event, gpointer data) {
//        CApplicationData AppData = new CApplicationData(data);
//
//        return AppData.DrawingAreaExpose(widget, event);
//    }
//
//    boolean DrawingAreaButtonPressEventCallback(GtkWidget*widget, GdkEventButton*event, gpointer data) {
//        CApplicationData AppData = new CApplicationData(data);
//
//        return AppData.DrawingAreaButtonPressEvent(widget, event);
//    }
//
//    boolean DrawingAreaButtonReleaseEventCallback(GtkWidget*widget, GdkEventButton*event, gpointer data) {
//        CApplicationData AppData = new CApplicationData(data);
//
//        return AppData.DrawingAreaButtonReleaseEvent(widget, event);
//    }
//
//    boolean DrawingAreaMotionNotifyEventCallback(GtkWidget*widget, GdkEventMotion*event, gpointer data) {
//        CApplicationData AppData = new CApplicationData(data);
//
//        return AppData.DrawingAreaMotionNotifyEvent(widget, event);
//    }
//
    /**
     * DGameMode is an enumeration to keep track of the current game mode (gmMainMenu,
     * gmbattle...). Determines what to do based on DGameMode.
     * Follows pattern: process, calculate, render
     * TODO: Add gmGameOver case
     *
     * @return boolean always TRUE
     **/
    private boolean Timeout() {
        switch (DGameMode) {
            case gmMainMenu:
            case gmOptionsMenu:
            case gmLocalMultiPlayerMenu:
            case gmOnlineMultiPlayerMenu:
            case gmMultiPlayerOptionsMenu:
            case gmGameMenu:
            case gmLANHostInfo:
            case gmNoServerResponse:
            case gmInvalidUser:
                ProcessInputButtonMenuMode();
                CalculateButtonMenuMode();
                break;
            case gmHostSelect:
                ProcessInputHostSelectMode();
                CalculateHostSelectMode();
                RenderHostSelectMode();
                break;
            case gmSoundOptions:
            case gmNetworkOptions:
            case gmGameOptions:
                ProcessInputEditOptionsMode();
                CalculateEditOptionsMode();
                RenderEditOptionsMode();
                break;
            case gmMapSelect:
//                ProcessInputMapSelectMode();
                CalculateMapSelectMode();
                RenderMapSelectMode();
                break;
            case gmPlayerAISelect:
                ProcessInputPlayerAISelectMode();
                CalculatePlayerAISelectMode();
                RenderPlayerAISelectMode();
                break;
            case gmMultiPlayerLobby:
                ProcessInputLobbyMode();
                CalculateLobbyMode();
                RenderLobbyMode();
                break;
            case gmBattle:
            default:
                DGameModel.ClearGameEvents();
                ProcessInputGameMode();
                CalculateGameMode();
                RenderGameMode();
                break;
            case gmGameOver:
                Log.d("GM", "Gaaaaaame Ovaaaaaaaaaaa");
                if (!GameOvered) {
                    if (DGameModel.DPlayers.get(DPlayerColor.ordinal()).IsAlive()) {
                        DMainActivity.gameOver(true);
                    } else {
                        DMainActivity.gameOver(false);
                    }
                    DGameMode = gmGameMenu;
                    DNextGameMode = gmGameMenu;
                    GameOvered = true;
                }
                break;

        }

        for (int Index = 0; Index < pcMax.ordinal(); Index++) {
            if (!DLeftDown[Index] || (2 != DLeftClick[Index])) {
                DLeftClick[Index] = 0;
            }
            DRightClick[Index] = 0;
        }
//
//        DCursorSet.DrawCursor(DWorkingBufferPixmap, DDrawingContext, DCurrentX[DPlayerColor
//                .ordinal()], DCurrentY[DPlayerColor.ordinal()], DCursorIndices[DCursorType
//                .ordinal()]);
//
//        gdk_draw_pixmap(DDoubleBufferPixmap, DDrawingContext, DWorkingBufferPixmap, 0, 0, 0, 0, -1, -1);
//
//        gdk_draw_pixmap(DDrawingArea.window, DDrawingContext, DDoubleBufferPixmap, 0, 0, 0, 0, -1, -1);
//
//        if (DNextGameMode != DGameMode) {
//            SwitchButtonMenuData();
//        }
        DGameMode = DNextGameMode;

        return true;
    }
//
//    private boolean MainWindowDeleteEvent(GtkWidget*widget, GdkEvent*event) {
//        Log.d("DEBUG_LOW",  "Delete event occurred\n");
//        DDeleted = true;
//        return FALSE;
//    }
//
//    private void MainWindowDestroy(GtkWidget*widget) {
//        gtk_main_quit();
//    }
//
//    private boolean MainWindowKeyPressEvent(GtkWidget*widget, GdkEventKey*event) {
//        boolean Found = false;
//        for (Long Key : DPressedKeys[pcNone.ordinal()]) {
//            if (Key == event.keyval) {
//                Found = true;
//                break;
//            }
//        }
//        if (!Found) {
//            DPressedKeys[pcNone.ordinal()].add(event.keyval);
//            DPressedKeys[DPlayerColor.ordinal()].add(event.keyval);
//        }
//        return TRUE;
//    }
//
//    private boolean MainWindowKeyReleaseEvent(GtkWidget*widget, GdkEventKey*event) {
//        boolean Found = false;
//        int Index = 0;
//        for (Long Key : DPressedKeys[pcNone.ordinal()]) {
//            if (Key == event.keyval) {
//                Found = true;
//                break;
//            }
//            Index++;
//        }
//        if (Found) {
//            DPressedKeys[pcNone.ordinal()].erase(DPressedKeys[pcNone.ordinal()].begin() + Index);
//            DPressedKeys[DPlayerColor.ordinal()].erase(DPressedKeys[DPlayerColor.ordinal()].begin() + Index);
//        }
//        Found = false;
//        for (Long Key : DReleasedKeys[pcNone.ordinal()]) {
//            if (Key == event.keyval) {
//                Found = true;
//                break;
//            }
//        }
//        if (!Found) {
//            DReleasedKeys[pcNone.ordinal()].add(event.keyval);
//            DReleasedKeys[DPlayerColor.ordinal()].add(event.keyval);
//        }
//        return TRUE;
//    }
//
//    private boolean MainWindowConfigureEvent(GtkWidget*widget, GdkEvent*event) {
//        ResizeCanvases();
//        return TRUE;
//    }
//
//    private boolean DrawingAreaExpose(GtkWidget*widget, GdkEventExpose*event) {
//        gdk_draw_pixmap(widget.window, widget.style.fg_gc[gtk_widget_get_state(widget)],
//                DDoubleBufferPixmap, event.area.x, event.area.y, event.area.x, event.area.y,
//                event.area.width, event.area.height);
//        return FALSE;
//    }
//
//    private boolean DrawingAreaButtonPressEvent(GtkWidget*widget, GdkEventButton*event) {
//        if (1 == event.button) {
//            if (GDK_CONTROL_MASK & event.state) {
//                DRightClick[pcNone.ordinal()] = DRightClick[DPlayerColor.ordinal()] =
//                        GDK_2BUTTON_PRESS == event.type ? 2 : 1;
//                DRightDown[pcNone.ordinal()] = DRightDown[DPlayerColor.ordinal()] = true;
//            } else {
//                DLeftClick[pcNone.ordinal()] = DLeftClick[DPlayerColor.ordinal()] =
//                        GDK_2BUTTON_PRESS == event.type ? 2 : 1;
//                DLeftDown[pcNone.ordinal()] = DLeftDown[DPlayerColor.ordinal()] = true;
//            }
//        }
//        if (3 == event.button) {
//            DRightClick[pcNone.ordinal()] = DRightClick[DPlayerColor.ordinal()] =
//                    GDK_2BUTTON_PRESS == event.type ? 2 : 1;
//            DRightDown[pcNone.ordinal()] = DRightDown[DPlayerColor.ordinal()] = true;
//        }
//        return TRUE;
//    }
//
//    private boolean DrawingAreaButtonReleaseEvent(GtkWidget*widget, GdkEventButton*event) {
//        if (1 == event.button) {
//            if (GDK_CONTROL_MASK & event.state) {
//                DRightClick[pcNone.ordinal()] = DRightClick[DPlayerColor.ordinal()] =
//                        GDK_2BUTTON_PRESS == event.type ? 2 : 1;
//                DRightDown[pcNone.ordinal()] = DRightDown[DPlayerColor.ordinal()] = false;
//            } else {
//                if (0 == DLeftClick[DPlayerColor.ordinal()]) {
//                    DLeftClick[pcNone.ordinal()] = DLeftClick[DPlayerColor.ordinal()] =
//                            GDK_2BUTTON_PRESS == event.type ? 2 : 1;
//                }
//                DLeftDown[pcNone.ordinal()] = DLeftDown[DPlayerColor.ordinal()] = false;
//            }
//        }
//        if (3 == event.button) {
//            DRightClick[pcNone.ordinal()] = DRightClick[DPlayerColor.ordinal()] =
//                    GDK_2BUTTON_PRESS == event.type ? 2 : 1;
//            DRightDown[pcNone.ordinal()] = DRightDown[DPlayerColor.ordinal()] = false;
//        }
//        return TRUE;
//    }
//
//    private boolean DrawingAreaMotionNotifyEvent(GtkWidget*widget, GdkEventMotion*event) {
//        GtkAllocation DrawingAreaAllocation;
//        int EventX, EventY;
//        EventX = event.x;
//        EventY = event.y;
//
//        gtk_widget_get_allocation(DDrawingArea, DrawingAreaAllocation);
//
//        if (EventX > DrawingAreaAllocation.width) {
//            EventX = DrawingAreaAllocation.width - 1;
//        }
//        if (0 > EventX) {
//            EventX = 0;
//        }
//        if (EventY > DrawingAreaAllocation.height) {
//            EventY = DrawingAreaAllocation.height - 1;
//        }
//        if (0 > EventY) {
//            EventY = 0;
//        }
//
//        DCurrentX[pcNone.ordinal()] = DCurrentX[DPlayerColor.ordinal()] = EventX;
//        DCurrentY[pcNone.ordinal()] = DCurrentY[DPlayerColor.ordinal()] = EventY;
//        return TRUE;
//    }
//
//    private EUIComponentType FindUIComponentType(CPosition pos) {
//        int ViewWidth, ViewHeight;
//        int MiniWidth, MiniHeight;
//        int DescrWidth, DescrHeight;
//        int ActWidth, ActHeight;
//
//        gdk_pixmap_get_size(DViewportPixmap, ViewWidth, ViewHeight);
//        if ((DViewportXOffset > pos.X()) || (DViewportYOffset > pos.Y()) || (DViewportXOffset +
//                ViewWidth <= pos.X()) || (DViewportYOffset + ViewHeight <= pos.Y())) {
//            if ((DViewportXOffset - DInnerBevel.Width() <= pos.X()) && (DViewportXOffset > pos.X())) {
//                if ((DViewportYOffset <= pos.Y()) && (pos.Y() < DViewportYOffset + ViewHeight)) {
//                    return uictViewportBevelW;
//                }
//            } else if ((DViewportXOffset + ViewWidth <= pos.X()) && (DViewportXOffset + ViewWidth +
//                    DInnerBevel.Width() > pos.X())) {
//                if ((DViewportYOffset <= pos.Y()) && (pos.Y() < DViewportYOffset + ViewHeight)) {
//                    return uictViewportBevelE;
//                }
//            } else if ((DViewportXOffset <= pos.X()) && (pos.X() < DViewportXOffset + ViewWidth)) {
//                if ((DViewportYOffset - DInnerBevel.Width() <= pos.Y()) && (DViewportYOffset > pos
//                        .Y())) {
//                    return uictViewportBevelN;
//                } else if ((DViewportYOffset + ViewHeight <= pos.Y()) && (DViewportYOffset +
//                        ViewHeight + DInnerBevel.Width() > pos.Y())) {
//                    return uictViewportBevelS;
//                }
//            }
//        } else {
//            return uictViewport;
//        }
//        gdk_pixmap_get_size(DMiniMapPixmap, MiniWidth, MiniHeight);
//        if ((DMiniMapXOffset <= pos.X()) && (DMiniMapXOffset + MiniWidth > pos.X()) &&
//                (DMiniMapYOffset <= pos.Y()) && (DMiniMapYOffset + MiniHeight > pos.Y())) {
//            return uictMiniMap;
//        }
//        gdk_pixmap_get_size(DUnitDescriptionPixmap, DescrWidth, DescrHeight);
//        if ((DUnitDescriptionXOffset <= pos.X()) && (DUnitDescriptionXOffset + DescrWidth > pos.X
//                ()) && (DUnitDescriptionYOffset <= pos.Y()) && (DUnitDescriptionYOffset +
//                DescrHeight > pos.Y())) {
//            return uictUserDescription;
//        }
//        gdk_pixmap_get_size(DUnitActionPixmap, ActWidth, ActHeight);
//        if ((DUnitActionXOffset <= pos.X()) && (DUnitActionXOffset + ActWidth > pos.X()) &&
//                (DUnitActionYOffset <= pos.Y()) && (DUnitActionYOffset + ActHeight > pos.Y())) {
//            return uictUserAction;
//        }
//
//        if ((DMenuButtonXOffset <= pos.X()) && (DMenuButtonXOffset + DMenuButtonRenderer.Width() >
//                pos.X()) && (DMenuButtonYOffset <= pos.Y()) && (DMenuButtonYOffset +
//                DMenuButtonRenderer.Height() > pos.Y())) {
//            return uictMenuButton;
//        }
//        return uictNone;
//    }

    private CPosition ScreenToViewport(CPosition pos) {
        return new CPosition(pos.X() - DViewportXOffset, pos.Y() - DViewportYOffset);
    }

    private CPosition ScreenToMiniMap(CPosition pos) {
        return new CPosition(pos.X() - DMiniMapXOffset, pos.Y() - DMiniMapYOffset);
    }

    private CPosition ScreenToDetailedMap(CPosition pos) {
        return ViewportToDetailedMap(ScreenToViewport(pos));
    }

    private CPosition ScreenToUnitDescription(CPosition pos) {
        return new CPosition(pos.X() - DUnitDescriptionXOffset, pos.Y() - DUnitDescriptionYOffset);
    }

    private CPosition ScreenToUnitAction(CPosition pos) {
        return new CPosition(pos.X() - DUnitActionXOffset, pos.Y() - DUnitActionYOffset);
    }

    private CPosition ViewportToDetailedMap(CPosition pos) {
        return DViewportRenderer.DetailedPosition(pos);
    }

    private CPosition MiniMapToDetailedMap(CPosition pos) {
        int X = pos.X() * DGameModel.Map().Width() / DMiniMapRenderer.VisibleWidth();
        int Y = pos.Y() * DGameModel.Map().Height() / DMiniMapRenderer.VisibleHeight();

        if (0 > X) {
            X = 0;
        }
        if (DGameModel.Map().Width() <= X) {
            X = DGameModel.Map().Width() - 1;
        }
        if (0 > Y) {
            Y = 0;
        }
        if (DGameModel.Map().Height() <= Y) {
            Y = DGameModel.Map().Height() - 1;
        }
        CPosition Temp = new CPosition();
        Temp.SetXFromTile(X);
        Temp.SetYFromTile(Y);
        return Temp;
    }

    public void ProcessInput(GameView gameView) {
        CPosition currPosition = new CPosition((int) (gameView.firstX + gameView.movX), (int)
                (gameView.firstY + gameView.movY));
        CPosition currTilePosition = new CPosition();
        currTilePosition.SetToTile(currPosition);

        Log.d("pos", "currPosition " + currPosition.X() + currPosition.Y());

        Log.d("MOVE!", "Input State" + String.valueOf(DInputState));

        CGameDataTypes.EPlayerColor SearchColor = DPlayerColor;
        CPlayerData currPlayer = DGameModel.Player(SearchColor);
        ArrayList<CPlayerAsset> selectedAssets = new ArrayList<>();

        CPlayerAsset targetAsset = null;
        for (CPlayerAsset Asset : DGameModel.DActualMap.Assets()) { // see if you tapped an asset
            if (currTilePosition.X() >= Asset.TilePositionX() && currTilePosition.X() < Asset
                    .TilePositionX() + Asset.DType.Size()) {
                if (currTilePosition.Y() >= Asset.TilePositionY() && currTilePosition.Y() < Asset
                        .TilePositionY() + Asset.DType.Size()) {
                    targetAsset = Asset;
                }
            }
        }

        Log.d("ProcessInput", "current capability: " + DCurrentAssetCapability[DPlayerColor.ordinal()]);
        // Currently works for selecting only one asset
        switch (DInputState) {
            case isSelected: // do right click actions (move, harvest, mine)
                if (DSelectedPlayerAssets.get(DPlayerColor.ordinal()) != null &&
                        !DSelectedPlayerAssets.get(DPlayerColor.ordinal()).isEmpty() &&
                        DSelectedPlayerAssets.get(DPlayerColor.ordinal()).get(0) != null &&
                        !gameView.pinching) {
                    if (DSelectedPlayerAssets.get(DPlayerColor.ordinal()).get(0).Capabilities()
                            .contains(actMove) && selectedCapability == null && DPlayerColor ==
                            DSelectedPlayerAssets.get(DPlayerColor.ordinal()).get(0).Color()) {
                        //TODO Tie this to a button press in the side panel
                        //TODO Make this work with the player capabilities.  The actual command to be
                        //executed should depend on the button press state

//                    if (pcNone != PixelType.Color()) {
                        if (targetAsset != null && targetAsset.DType().Type() != atGoldMine) { // target is an asset (not goldmine)
                            // Command is either walk/deliver, repair, or attack
                            DPlayerCommands[DPlayerColor.ordinal()].DAction = actMove;
                            DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = targetAsset
                                    .DType().Color();//PixelType.Color();
                            DPlayerCommands[DPlayerColor.ordinal()].DTargetType = targetAsset
                                    .DType().Type();//PixelType.AssetType();
                            DPlayerCommands[DPlayerColor.ordinal()].DActors =
                                    DSelectedPlayerAssets.get(DPlayerColor.ordinal());
                            DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = currPosition;//TempPosition;
//                        if (PixelType.Color() == DPlayerColor) {
                            if (targetAsset.DType().Color() == DPlayerColor) {
                                boolean HaveLumber = false;
                                boolean HaveGold = false;

                                for (CPlayerAsset Asset : DSelectedPlayerAssets.get(DPlayerColor
                                        .ordinal())) {
                                    CPlayerAsset LockedAsset = Asset;
                                    if (LockedAsset != null) {
                                        if (LockedAsset.Lumber() > 0) {
                                            HaveLumber = true;
                                        }
                                        if (LockedAsset.Gold() > 0) {
                                            HaveGold = true;
                                        }
                                    }
                                }
                                if (HaveGold) {
                                    if ((atTownHall == DPlayerCommands[DPlayerColor.ordinal()]
                                            .DTargetType) || (atKeep ==
                                            DPlayerCommands[DPlayerColor.ordinal()].DTargetType)
                                            || (atCastle == DPlayerCommands[DPlayerColor.ordinal
                                            ()].DTargetType)) {

                                        DPlayerCommands[DPlayerColor.ordinal()].DAction = actConvey;
                                    }
                                } else if (HaveLumber) {
                                    if ((atTownHall == DPlayerCommands[DPlayerColor.ordinal()]
                                            .DTargetType) || (atKeep ==
                                            DPlayerCommands[DPlayerColor.ordinal()].DTargetType)
                                            || (atCastle == DPlayerCommands[DPlayerColor.ordinal
                                            ()].DTargetType) || (atLumberMill ==
                                            DPlayerCommands[DPlayerColor.ordinal()].DTargetType)) {
                                        DPlayerCommands[DPlayerColor.ordinal()].DAction = actConvey;
                                    }
                                } else if (targetAsset.DType().Type() == atPeasant || targetAsset
                                        .DType().Type() == atTownHall) { //temporary
                                    // hard code to test attacking
                                    DPlayerCommands[DPlayerColor.ordinal()].DAction = actAttack;
                                } else {
                                    CPlayerAsset TargetAsset = DGameModel.Player(DPlayerColor)
                                            .SelectAsset(currPosition, targetAsset.DType().Type());//PixelType.AssetType());
                                    if ((0 == TargetAsset.Speed()) && (TargetAsset.MaxHitPoints()
                                            > TargetAsset.HitPoints())) {
                                        DPlayerCommands[DPlayerColor.ordinal()].DAction = actRepair;
                                    }
                                }
                            } else {
                                DPlayerCommands[DPlayerColor.ordinal()].DAction = actAttack;
                            }

                            DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
                        } else { // target is tree, terrain, or goldmine
                            boolean CanHarvest = true;

                            DPlayerCommands[DPlayerColor.ordinal()].DAction = actMove;
                            DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = pcNone;
                            DPlayerCommands[DPlayerColor.ordinal()].DTargetType = atNone;
                            DPlayerCommands[DPlayerColor.ordinal()].DActors =
                                    DSelectedPlayerAssets.get(DPlayerColor.ordinal());
                            DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = currPosition;

                            for (CPlayerAsset Asset : DSelectedPlayerAssets.get(DPlayerColor
                                    .ordinal())) {
                                if (!Asset.HasCapability(actMine)) {
                                    CanHarvest = false;
                                    break;
                                }
                            }
                            if (CanHarvest) {
                                if (DGameModel.DActualMap.TileType(currTilePosition) == ttTree) {
                                    DPlayerCommands[DPlayerColor.ordinal()].DAction = actMine;

                                    if (ttTree != DGameModel.Player(DPlayerColor).PlayerMap()
                                            .TileType(currTilePosition)) {
                                        // Could be tree pixel, but tops of next row
                                        currTilePosition.IncrementY(1);
                                        if (ttTree == DGameModel.Player(DPlayerColor).PlayerMap()
                                                .TileType(currTilePosition)) {
                                            DPlayerCommands[DPlayerColor.ordinal()]
                                                    .DTargetLocation.SetFromTile(currTilePosition);
                                        }
                                    }
                                } else if (targetAsset != null && atGoldMine == targetAsset.DType
                                        ().Type()) {
                                    DPlayerCommands[DPlayerColor.ordinal()].DAction = actMine;
                                    DPlayerCommands[DPlayerColor.ordinal()].DTargetType =
                                            atGoldMine;
                                    DCurrentAssetCapability[DPlayerColor.ordinal()] = actMine;
                                }
                            }
                            DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
                        }
                        DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
                        DInputState = EInputState.isNone;
                    }
                }
                // Drop through allows for a new asset to be selected if another tap is made
            case isNone:  //Base case, our current selection code
                for (CPlayerAsset Asset : DGameModel.DActualMap.Assets()) {
                    if (gameView.pinching) { // select multiple units
                        if (Asset.Speed() > 0) {
                            if (Asset.PositionX() >= (Math.min(gameView.firstX, gameView.secX) +
                                    gameView.movX) && Asset.PositionX() <= (Math.max(gameView
                                    .firstX, gameView.secX) + gameView.movX)) {
                                if ((Asset.PositionY() >= (Math.min(gameView.firstY, gameView
                                        .secY) + gameView.movY) && Asset.PositionY() <= (Math.max
                                        (gameView.firstY, gameView.secY) + gameView.movY)) &&
                                        Asset.DType().Color() == DPlayerColor) {
                                    selectedAssets.add(Asset);
                                    DSelectedPlayerAssets.set(DPlayerColor.ordinal(),
                                            selectedAssets);
                                    DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
                                    SGameEvent TempEvent = new SGameEvent();
                                    TempEvent.DType = etSelection;
                                    TempEvent.DAsset = Asset;
                                    DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);
                                    DInputState = EInputState.isSelected;
                                    if (DPlayerColor != Asset.Color()) {
                                        ArrayList<CPlayerAsset> selectionList = new ArrayList<>();
                                        EAssetCapabilityType currentaction = null;
                                        DUnitActionRenderer.DrawUnitAction(selectionList,
                                                currentaction);
                                    } else if (DPlayerColor == Asset.Color()) {
                                        DUnitActionRenderer.DrawUnitAction(DSelectedPlayerAssets
                                                .get(DPlayerColor.ordinal()),
                                                DCurrentAssetCapability[DPlayerColor.ordinal()]);
                                    }
                                }
                            }
                        }
                    } else if (currTilePosition.X() >= Asset.TilePositionX() && currTilePosition
                            .X() < Asset.TilePositionX() + Asset.DType.Size()) {
                        if (currTilePosition.Y() >= Asset.TilePositionY() && currTilePosition.Y() <
                                Asset.TilePositionY() + Asset.DType.Size()) {
                            selectedAssets.add(Asset);
                            DSelectedPlayerAssets.set(DPlayerColor.ordinal(), selectedAssets);
                            DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
                            SGameEvent TempEvent = new SGameEvent();
                            TempEvent.DType = etSelection;
                            TempEvent.DAsset = Asset;
                            DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);
                            DInputState = EInputState.isSelected;
                            if (DPlayerColor != Asset.Color()) {
                                ArrayList<CPlayerAsset> selectionList = new ArrayList<>();
                                EAssetCapabilityType currentaction = null;
                                DUnitActionRenderer.DrawUnitAction(selectionList, currentaction);
                            } else if (DPlayerColor == Asset.Color()) {
                                DUnitActionRenderer.DrawUnitAction(DSelectedPlayerAssets.get
                                        (DPlayerColor.ordinal()),
                                        DCurrentAssetCapability[DPlayerColor.ordinal()]);
                            }
                        }
                    }
                }

                DUnitDescriptionRenderer.DrawDescriptions(DSelectedPlayerAssets
                        .get(DApplicationData.DPlayerColor.ordinal()));
                break;
            case isCommand:
                Log.d("PATH", "In isCommand");
                if (selectedCapability != null) { // if capability is selected
                    Log.d("PATH", "selectedCapability is not null");
                    Log.d("PATH", "it is :: " + selectedCapability);
                    CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability
                            (selectedCapability);
                    if (PlayerCapability != null) {
                        Log.d("PATH", "PlayerCapability is not null :: " + PlayerCapability );
                        if (ETargetType.ttNone.ordinal() == PlayerCapability.TargetType().ordinal
                                () || ETargetType.ttPlayer.ordinal() == PlayerCapability
                                .TargetType().ordinal()) {
                            CPlayerAsset ActorTarget = DSelectedPlayerAssets.get(DPlayerColor
                                    .ordinal()).get(0);
                            if (PlayerCapability.CanApply(ActorTarget, DGameModel.Player
                                    (DPlayerColor), ActorTarget) && (DPlayerColor ==
                                    ActorTarget.Color())) {
                                DPlayerCommands[DPlayerColor.ordinal()].DAction =
                                        selectedCapability;
                                DPlayerCommands[DPlayerColor.ordinal()].DActors =
                                        DSelectedPlayerAssets.get(DPlayerColor.ordinal());
                                DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = pcNone;
                                DPlayerCommands[DPlayerColor.ordinal()].DTargetType = atNone;
                                DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation =
                                        ActorTarget.Position();
                                DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
                            }
                        } else {
                            DCurrentAssetCapability[DPlayerColor.ordinal()] = selectedCapability;
                            DInputState = isTarget;
                        }

                    } else {
                        DCurrentAssetCapability[DPlayerColor.ordinal()] = selectedCapability;
                    }

                    // Switch to completion fragment
                }
                if (DInputState != isTarget) {
                    DInputState = isNone;
                    selectedCapability = null;
                }

                DUnitActionRenderer.DrawUnitAction(DApplicationData.DSelectedPlayerAssets.get
                        (DApplicationData.DPlayerColor.ordinal()), DApplicationData
                        .DCurrentAssetCapability[DApplicationData.DPlayerColor.ordinal()]);
                DUnitDescriptionRenderer.DrawDescriptions(DSelectedPlayerAssets
                        .get(DApplicationData.DPlayerColor.ordinal()));
                break;

            case isTarget:
                CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability
                        (selectedCapability);
                if (((ttAsset == PlayerCapability.TargetType()) || (ttTerrainOrAsset ==
                        PlayerCapability.TargetType())) && (targetAsset != null) &&
                        (atNone != targetAsset.Type())) {
                    CPlayerAsset NewTarget = DGameModel.Player(targetAsset.Color()).SelectAsset
                            (currPosition, targetAsset.Type());//.lock();
                    if (PlayerCapability.CanApply(DSelectedPlayerAssets.get(DPlayerColor.ordinal()
                    ).get(0), DGameModel.Player(DPlayerColor), NewTarget) ) {

                        SGameEvent TempEvent = new SGameEvent();
                        TempEvent.DType = etPlaceAction;
                        TempEvent.DAsset = NewTarget;
                        DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);

                        DPlayerCommands[DPlayerColor.ordinal()].DAction =
                                DCurrentAssetCapability[DPlayerColor.ordinal()];
                        DPlayerCommands[DPlayerColor.ordinal()].DActors = DSelectedPlayerAssets
                                .get(DPlayerColor.ordinal());
                        DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = targetAsset.Color();
                        DPlayerCommands[DPlayerColor.ordinal()].DTargetType = targetAsset.Type();
                        DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = currPosition;
                        DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
                    }
                } else if (((ETargetType.ttTerrain.ordinal() == PlayerCapability.TargetType()
                        .ordinal()) || (ttTerrainOrAsset.ordinal() ==
                        PlayerCapability.TargetType().ordinal())) && targetAsset == null) {
                    CPlayerAsset NewTarget = DGameModel.Player(DPlayerColor).CreateMarker
                            (currTilePosition, false);

                    if (PlayerCapability.CanApply(DSelectedPlayerAssets.get(DPlayerColor.ordinal
                            ()).get(0), DGameModel.Player(DPlayerColor), NewTarget)) {

                        SGameEvent TempEvent = new SGameEvent();
                        TempEvent.DType = etPlaceAction;
                        TempEvent.DAsset = NewTarget;
                        DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);

                        DPlayerCommands[DPlayerColor.ordinal()].DAction =
                                DCurrentAssetCapability[DPlayerColor.ordinal()];
                        DPlayerCommands[DPlayerColor.ordinal()].DActors = DSelectedPlayerAssets
                                .get(DPlayerColor.ordinal());
                        DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = pcNone;
                        DPlayerCommands[DPlayerColor.ordinal()].DTargetType = atNone;
                        DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = currPosition;
                        DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
                    }
                }
                DInputState = isNone;
                selectedCapability = null;
        }
    }

    /**
     * Part 1 of Gamelogic.
     * Handles arrowkeys, shift, esc.
     * It deals with one event or key press at a time.
     * Creates commands (walk, mine, harvest, deliver, repair, attack) and fills DPlayerCommands.
     */
    private void ProcessInputGameMode() {

        //Clear Player Commands to remove copies of commands being sent to server
        DPlayerCommands[DPlayerColor.ordinal()].DAction = EAssetCapabilityType.values()[0];
        DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = pcNone;
        DPlayerCommands[DPlayerColor.ordinal()].DTargetType = CGameDataTypes.EAssetType.values()[0];
        DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = new CPosition();
        DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation.X(0);
        DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation.Y(0);
        DPlayerCommands[DPlayerColor.ordinal()].DActors = new ArrayList<CPlayerAsset>();
        DPlayerCommands[DPlayerColor.ordinal()].DActors.clear();

        // Declare Asset Capability in case needs to be rendered
        EAssetCapabilityType CursorOverAsset = actNone;

        // Maybe change this seems wastefull function call - as of right not this removes
        // the action when you scroll out of the action box but not onto another one
        DGameModel.Player(DPlayerColor).ActionToRender(CursorOverAsset);

        boolean Panning = false;
        boolean ShiftPressed = false;
        boolean CtrlPressed = false;
        int NumPressed = -1;
        CGameDataTypes.EDirection PanningDirection = CGameDataTypes.EDirection.dMax;

        // Handle panning with arrow keys, ctrl, & shift
//        for (auto Key : DPressedKeys[DPlayerColor]) {
//            if (GDK_KEY_Up == Key) {
//                PanningDirection = dNorth;
//                Panning = true;
//            }
//            else if (GDK_KEY_Down == Key) {
//                PanningDirection = dSouth;
//                Panning = true;
//            }
//            else if (GDK_KEY_Left == Key) {
//                PanningDirection = dWest;
//                Panning = true;
//            }
//            else if (GDK_KEY_Right == Key) {
//                PanningDirection = dEast;
//                Panning = true;
//            }
//            else if ((GDK_KEY_Shift_L == Key) || (GDK_KEY_Shift_R == Key)) {
//                ShiftPressed = true;
//            }
//            else if (GDK_KEY_Control_L == Key || GDK_KEY_Control_R == Key) {
//                CtrlPressed = true;
//            }
//            else {
//                switch(Key) {//check for number presses
//                    case GDK_KEY_1: NumPressed = 1; break;
//                    case GDK_KEY_2: NumPressed = 2; break;
//                    case GDK_KEY_3: NumPressed = 3; break;
//                    case GDK_KEY_4: NumPressed = 4; break;
//                    case GDK_KEY_5: NumPressed = 5; break;
//                    case GDK_KEY_6: NumPressed = 6; break;
//                    case GDK_KEY_7: NumPressed = 7; break;
//                    case GDK_KEY_8: NumPressed = 8; break;
//                    case GDK_KEY_9: NumPressed = 9; break;
//                    case GDK_KEY_0: NumPressed = 0; break;
//                }
//            }
//        }
//
//        for (auto Key : DReleasedKeys[DPlayerColor]) {
//            // Handle releases
//            if (DSelectedPlayerAssets[DPlayerColor].size()) {//check asset size
//                bool CanMove = true;
//                for (auto &Asset : DSelectedPlayerAssets[DPlayerColor]) {//go through assets
//                    if (auto LockedAsset = Asset.lock()) {
//                        if (0 == LockedAsset->Speed()) {//if speed = 0, this asset can't move
//                            CanMove = false;
//                            break;
//                        }
//                    }
//                }
//                if (GDK_KEY_Escape == Key) {//if clicked escape key, no action
//                    DCurrentAssetCapability[DPlayerColor] = actNone;
//                }
//
//                if (actBuildSimple == DCurrentAssetCapability[DPlayerColor]) {//if currently selected ability is building
//                    auto KeyLookup = DBuildHotKeyMap.find(Key);
//                    // check build
//                    if (KeyLookup != DBuildHotKeyMap.end()) {
//                        auto PlayerCapability = CPlayerCapability::FindCapability(KeyLookup->second);
//
//                        if (PlayerCapability) {
//                            auto ActorTarget = DSelectedPlayerAssets[DPlayerColor].front().lock();
//
//                            // if actor speed > 0
//                            if (PlayerCapability->CanInitiate(ActorTarget, DGameModel->Player(DPlayerColor))) {
//                                SGameEvent TempEvent;
//                                TempEvent.DType = etButtonTick;
//                                DGameModel->Player(DPlayerColor)->AddGameEvent(TempEvent);
//
//                                DCurrentAssetCapability[DPlayerColor] = KeyLookup->second;
//                            }
//                        }
//                    }
//                }
//                // All selected assets can move
//                else if (CanMove) {//if is a mobile asset e.g. peasants
//                    auto KeyLookup = DUnitHotKeyMap.find(Key);//hot key press
//
//                    if (KeyLookup != DUnitHotKeyMap.end()) {
//                        bool HasCapability = true;
//                        for (auto &Asset : DSelectedPlayerAssets[DPlayerColor]) {//go through player's assets
//                            if (auto LockedAsset = Asset.lock()) {//lock it down
//                                if (!LockedAsset->HasCapability(KeyLookup->second)) {//does it have this capability
//                                    HasCapability = false;
//                                    break;
//                                }
//                            }
//                        }
//                        // If selected mobile unit has capability
//                        if (HasCapability) {
//                            auto PlayerCapability = CPlayerCapability::FindCapability(KeyLookup->second);
//                            SGameEvent TempEvent;
//                            TempEvent.DType = etButtonTick;
//                            DGameModel->Player(DPlayerColor)->AddGameEvent(TempEvent);
//
//                            if (PlayerCapability) {
//                                if ((CPlayerCapability::ttNone == PlayerCapability->TargetType()) || (CPlayerCapability::ttPlayer == PlayerCapability->TargetType())) {
//                                    auto ActorTarget = DSelectedPlayerAssets[DPlayerColor].front().lock();
//
//                                    if (PlayerCapability->CanApply(ActorTarget, DGameModel->Player(DPlayerColor), ActorTarget)) {
//
//                                        DPlayerCommands[DPlayerColor].DAction = KeyLookup->second;
//                                        DPlayerCommands[DPlayerColor].DActors = DSelectedPlayerAssets[DPlayerColor];
//                                        DPlayerCommands[DPlayerColor].DTargetColor = pcNone;
//                                        DPlayerCommands[DPlayerColor].DTargetType = atNone;
//                                        DPlayerCommands[DPlayerColor].DTargetLocation = ActorTarget->TilePosition();
//                                        DCurrentAssetCapability[DPlayerColor] = actNone;
//                                    }
//                                }
//                                else {
//                                    DCurrentAssetCapability[DPlayerColor] = KeyLookup->second;
//                                }
//                            }
//                            else {
//                                DCurrentAssetCapability[DPlayerColor] = KeyLookup->second;
//                            }
//                        }
//                    }
//                }//if asset CANNOT move
//                else {
//                    auto KeyLookup = DTrainHotKeyMap.find(Key);//check hotkeys
//
//                    if (KeyLookup != DTrainHotKeyMap.end()) {//if asset has any abilities at all
//                        bool HasCapability = true;
//                        for (auto &Asset : DSelectedPlayerAssets[DPlayerColor]) {//check assets
//                            if (auto LockedAsset = Asset.lock()) {
//                                if (!LockedAsset->HasCapability(KeyLookup->second)) {//if asset does NOT have the selected ability
//                                    HasCapability = false;
//                                    break;
//                                }
//                            }
//                        }
//                        if (HasCapability) {
//                            auto PlayerCapability = CPlayerCapability::FindCapability(KeyLookup->second);
//                            SGameEvent TempEvent;
//                            TempEvent.DType = etButtonTick;
//                            DGameModel->Player(DPlayerColor)->AddGameEvent(TempEvent);
//
//                            if (PlayerCapability) {
//                                if ((CPlayerCapability::ttNone == PlayerCapability->TargetType()) || (CPlayerCapability::ttPlayer == PlayerCapability->TargetType())) {
//                                    auto ActorTarget = DSelectedPlayerAssets[DPlayerColor].front().lock();
//
//                                    if (PlayerCapability->CanApply(ActorTarget, DGameModel->Player(DPlayerColor), ActorTarget)) {
//
//                                        DPlayerCommands[DPlayerColor].DAction = KeyLookup->second;
//                                        DPlayerCommands[DPlayerColor].DActors = DSelectedPlayerAssets[DPlayerColor];
//                                        DPlayerCommands[DPlayerColor].DTargetColor = pcNone;
//                                        DPlayerCommands[DPlayerColor].DTargetType = atNone;
//                                        DPlayerCommands[DPlayerColor].DTargetLocation = ActorTarget->TilePosition();
//                                        DCurrentAssetCapability[DPlayerColor] = actNone;
//                                    }
//                                }
//                                else {
//                                    DCurrentAssetCapability[DPlayerColor] = KeyLookup->second;
//                                }
//                            }
//                            else {
//                                DCurrentAssetCapability[DPlayerColor] = KeyLookup->second;
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        DReleasedKeys[pcNone].clear();
//        DReleasedKeys[DPlayerColor].clear();
//
//        DMenuButtonState = CButtonRenderer::bsNone;
//        EUIComponentType ComponentType = FindUIComponentType(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor]));
//        if (uictViewport == ComponentType) {
//            CPosition TempPosition(ScreenToDetailedMap(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor])));//set temp position
//            CPixelType PixelType = CPixelType::GetPixelType(DViewportTypePixmap, ScreenToViewport(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor])));//What are we pointing at?
//
//            if (DRightClick[DPlayerColor] && !DRightDown[DPlayerColor] && DSelectedPlayerAssets[DPlayerColor].size()) {//if clicked and not the first frame of clicking
//                bool CanMove = true;
//                for (auto &Asset : DSelectedPlayerAssets[DPlayerColor]) {
//                    if (auto LockedAsset = Asset.lock()) {
//                        if (0 == LockedAsset->Speed()) {//if no speed can't move
//                            CanMove = false;
//                            break;
//                        }
//                    }
//                }
//                if (CanMove) {
//                    if (pcNone != PixelType.Color()) {
//                        // Command is either walk/deliver, repair, or attack
//
//                        DPlayerCommands[DPlayerColor].DAction = actMove;
//                        DPlayerCommands[DPlayerColor].DTargetColor = PixelType.Color();
//                        DPlayerCommands[DPlayerColor].DTargetType = PixelType.AssetType();
//                        DPlayerCommands[DPlayerColor].DActors = DSelectedPlayerAssets[DPlayerColor];
//                        DPlayerCommands[DPlayerColor].DTargetLocation = TempPosition;
//                        if (PixelType.Color() == DPlayerColor) {
//                            bool HaveLumber = false;
//                            bool HaveGold = false;
//                            bool CanHarvest = true;
//
//                            for (auto &Asset : DSelectedPlayerAssets[DPlayerColor]) {//in this assets capabilities
//                                if (auto LockedAsset = Asset.lock()) {
//                                    if (!LockedAsset->HasCapability(actMine)) {//if it does NOT have the ability to mine
//                                        CanHarvest = false;//it cannot harvest
//                                        break;
//                                    }
//                                }
//                            }
//
//                            for (auto &Asset : DSelectedPlayerAssets[DPlayerColor]) {
//                                if (auto LockedAsset = Asset.lock()) {
//                                    if (LockedAsset->Lumber()) {
//                                        HaveLumber = true;
//                                    }
//                                    if (LockedAsset->Gold()) {
//                                        HaveGold = true;
//                                    }
//                                }
//                            }
//                            if (HaveGold) {//if holding onto gold
//                                if ((atTownHall == DPlayerCommands[DPlayerColor].DTargetType) || (atKeep == DPlayerCommands[DPlayerColor].DTargetType) || (atCastle == DPlayerCommands[DPlayerColor].DTargetType)) {
//                                    DPlayerCommands[DPlayerColor].DAction = actConvey;
//                                }
//                            }
//                            else if (HaveLumber) {//if currently holding onto lumber
//                                if ((atTownHall == DPlayerCommands[DPlayerColor].DTargetType) || (atKeep == DPlayerCommands[DPlayerColor].DTargetType) || (atCastle == DPlayerCommands[DPlayerColor].DTargetType) || (atLumberMill == DPlayerCommands[DPlayerColor].DTargetType)) {
//                                    DPlayerCommands[DPlayerColor].DAction = actConvey;//next action is to carry the lumber
//                                }
//                            }
//                            else if (CanHarvest) {//this asset can harvest
//                                if (CPixelType::attGoldMine == PixelType.Type()) {
//                                    DPlayerCommands[DPlayerColor].DAction = actMine;
//                                    DPlayerCommands[DPlayerColor].DTargetType = atGoldMine;
//                                }
//                            }
//                            else {
//                                auto TargetAsset = DGameModel->Player(DPlayerColor)->SelectAsset(TempPosition, PixelType.AssetType()).lock();
//                                if ((0 == TargetAsset->Speed()) && (TargetAsset->MaxHitPoints() > TargetAsset->HitPoints())) {
//                                    DPlayerCommands[DPlayerColor].DAction = actRepair;//next action is to repair an object
//                                }
//
//                            }
//                        }
//                        else {
//                            DPlayerCommands[DPlayerColor].DAction = actAttack;//if none of the above, attack instead
//                        }
//                        DCurrentAssetCapability[DPlayerColor] = actNone;//if all else fails do nothing
//                    }
//                    else {
//                        // Command is either walk, mine, harvest
//                        CPosition TempPosition(ScreenToDetailedMap(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor])));
//                        bool CanHarvest = true;
//
//                        DPlayerCommands[DPlayerColor].DAction = actMove;
//                        DPlayerCommands[DPlayerColor].DTargetColor = pcNone;
//                        DPlayerCommands[DPlayerColor].DTargetType = atNone;
//                        DPlayerCommands[DPlayerColor].DActors = DSelectedPlayerAssets[DPlayerColor];
//                        DPlayerCommands[DPlayerColor].DTargetLocation = TempPosition;
//
//                        for (auto &Asset : DSelectedPlayerAssets[DPlayerColor]) {//in this assets capabilities
//                            if (auto LockedAsset = Asset.lock()) {
//                                if (!LockedAsset->HasCapability(actMine)) {//if it does NOT have the ability to mine
//                                    CanHarvest = false;//it cannot harvest
//                                    break;
//                                }
//                            }
//                        }
//
//                        if (CanHarvest) {//this asset can harvest
//                            if (CPixelType::attTree == PixelType.Type()) {//this asset is at a tree
//                                CPosition TempTilePosition;
//
//                                DPlayerCommands[DPlayerColor].DAction = actMine;//Use mine action to gather lumber
//                                TempTilePosition.SetToTile(DPlayerCommands[DPlayerColor].DTargetLocation);
//                                if (CTerrainMap::ttTree != DGameModel->Player(DPlayerColor)->PlayerMap()->TileType(TempTilePosition)) {
//                                    // Could be tree pixel, but tops of next row
//                                    TempTilePosition.IncrementY(1);
//                                    if (CTerrainMap::ttTree == DGameModel->Player(DPlayerColor)->PlayerMap()->TileType(TempTilePosition)) {
//                                        DPlayerCommands[DPlayerColor].DTargetLocation.SetFromTile(TempTilePosition);
//                                    }
//                                }
//                            }
//                            else if (CPixelType::attGoldMine == PixelType.Type()) {
//                                DPlayerCommands[DPlayerColor].DAction = actMine;
//                                DPlayerCommands[DPlayerColor].DTargetType = atGoldMine;
//                            } else if (CPixelType::attGold == PixelType.Type()) {
//                                DPlayerCommands[DPlayerColor].DAction = actMine;
//                                DPlayerCommands[DPlayerColor].DTargetType = atGold;
//                            } else if (CPixelType::attLumber == PixelType.Type()) {
//                                DPlayerCommands[DPlayerColor].DAction = actMine;
//                                DPlayerCommands[DPlayerColor].DTargetType = atLumber;
//                            }
//                        }
//                        DCurrentAssetCapability[DPlayerColor] = actNone;
//                    }
//                }
//            }
//
//            else if (DLeftClick[DPlayerColor] || -1 < NumPressed) {  //if left clicked or selection
//                if ((actNone == DCurrentAssetCapability[DPlayerColor]) || (actBuildSimple == DCurrentAssetCapability[DPlayerColor])) {
//                    if (DLeftDown[DPlayerColor]) {  //if first frame of clicking
//                        DMouseDown[DPlayerColor] = TempPosition;//set temp position
//                    }
//                    else if (DLeftClick[DPlayerColor]) {  //create a rectangle for selecting multiple units
//                        SRectangle TempRectangle;
//                        EPlayerColor SearchColor = DPlayerColor;
//                        std::list< std::shared_ptr< CPlayerAsset > > PreviousSelections;
//
//                        for (auto WeakAsset : DSelectedPlayerAssets[DPlayerColor]) {
//                            if (auto LockedAsset = WeakAsset.lock()) {
//                                PreviousSelections.push_back(LockedAsset);//add assets to list
//                            }
//                        }
//
//                        TempRectangle.DXPosition = MIN(DMouseDown[DPlayerColor].X(), TempPosition.X());
//                        TempRectangle.DYPosition = MIN(DMouseDown[DPlayerColor].Y(), TempPosition.Y());
//                        TempRectangle.DWidth = MAX(DMouseDown[DPlayerColor].X(), TempPosition.X()) - TempRectangle.DXPosition;
//                        TempRectangle.DHeight = MAX(DMouseDown[DPlayerColor].Y(), TempPosition.Y()) - TempRectangle.DYPosition;
//
//                        if ((TempRectangle.DWidth < CPosition::TileWidth()) || (TempRectangle.DHeight < CPosition::TileHeight()) || (2 == DLeftClick[DPlayerColor])) {
//                            TempRectangle.DXPosition = TempPosition.X();
//                            TempRectangle.DYPosition = TempPosition.Y();
//                            TempRectangle.DWidth = 0;
//                            TempRectangle.DHeight = 0;
//                            SearchColor = PixelType.Color();
//                        }
//                        if (SearchColor != DPlayerColor) {
//                            DSelectedPlayerAssets[DPlayerColor].clear();
//                        }
//
//                        if (ShiftPressed) {//add to the list
//                            if (!DSelectedPlayerAssets[DPlayerColor].empty()) {//selected asset is not empty
//                                if (auto TempAsset = DSelectedPlayerAssets[DPlayerColor].front().lock()) {
//                                    if (TempAsset->Color() != DPlayerColor) {
//                                        DSelectedPlayerAssets[DPlayerColor].clear();
//                                    }
//                                }
//                            }
//                            DSelectedPlayerAssets[DPlayerColor].splice(DSelectedPlayerAssets[DPlayerColor].end(), DGameModel->Player(SearchColor)->SelectAssets(TempRectangle, PixelType.AssetType(), 2 == DLeftClick[DPlayerColor]));//splice together the new list
//                            DSelectedPlayerAssets[DPlayerColor].sort(WeakPtrCompare<CPlayerAsset>);//sort the assets
//                            DSelectedPlayerAssets[DPlayerColor].unique(WeakPtrEquals<CPlayerAsset>);//make sure assets are unique
//                        }//if shift not pressed
//                        else {
//                            PreviousSelections.clear();//clear the selection
//                            DSelectedPlayerAssets[DPlayerColor] = DGameModel->Player(SearchColor)->SelectAssets(TempRectangle, PixelType.AssetType(), 2 == DLeftClick[DPlayerColor]);//add in a new selection
//                        }
//                        for (auto WeakAsset : DSelectedPlayerAssets[DPlayerColor]) {//each selected asset
//                            if (auto LockedAsset = WeakAsset.lock()) {
//                                bool FoundPrevious = false;//if found previously
//                                for (auto PrevAsset : PreviousSelections) {
//                                    if (PrevAsset == LockedAsset) {//if asset is locked, it was found before
//                                        FoundPrevious = true;//set this to true
//                                        break;
//                                    }
//                                }
//                                if (!FoundPrevious) {//if not
//                                    SGameEvent TempEvent;
//                                    TempEvent.DType = etSelection;//add a new event for the selection
//                                    TempEvent.DAsset = LockedAsset;
//                                    DGameModel->Player(DPlayerColor)->AddGameEvent(TempEvent);
//                                }
//                            }
//                        }
//
//
//                        DMouseDown[DPlayerColor] = CPosition(-1,-1);
//                    }//if clicking
//                    else {
//                        if (CtrlPressed && -1 < NumPressed) {//if ctrl and number
//                            DGroupedPlayerAssets[DPlayerColor][NumPressed] = DSelectedPlayerAssets[DPlayerColor];
//                        }
//                        else if (-1 < NumPressed) {
//                            DSelectedPlayerAssets[DPlayerColor].clear();
//                            DSelectedPlayerAssets[DPlayerColor] = DGroupedPlayerAssets[DPlayerColor][NumPressed];
//                            for (auto WeakAsset : DSelectedPlayerAssets[DPlayerColor]) {//each selected asset
//                                if (auto LockedAsset = WeakAsset.lock()) {
//                                    SGameEvent TempEvent;
//                                    TempEvent.DType = etSelection;//add a new event for the selection
//                                    TempEvent.DAsset = LockedAsset;
//                                    DGameModel->Player(DPlayerColor)->AddGameEvent(TempEvent);
//                                }
//                            }
//                        }
//                    }//else
//                    DCurrentAssetCapability[DPlayerColor] = actNone;
//                }//if not taking any action || simple building
//                else {
//                    auto PlayerCapability = CPlayerCapability::FindCapability(DCurrentAssetCapability[DPlayerColor]);
//
//                    if (PlayerCapability && !DLeftDown[DPlayerColor]) {
//                        if (((CPlayerCapability::ttAsset == PlayerCapability->TargetType()) || (CPlayerCapability::ttTerrainOrAsset == PlayerCapability->TargetType())) && (atNone != PixelType.AssetType())) {
//                            auto NewTarget = DGameModel->Player(PixelType.Color())->SelectAsset(TempPosition, PixelType.AssetType()).lock();
//
//                            if (PlayerCapability->CanApply(DSelectedPlayerAssets[DPlayerColor].front().lock(), DGameModel->Player(DPlayerColor), NewTarget)) {
//                                //PlayerCapability->ApplyCapability(DSelectedPlayerAssets[DPlayerColor].front().lock(), DGameModel->Player(DPlayerColor), NewTarget);
//                                SGameEvent TempEvent;
//                                TempEvent.DType = etPlaceAction;
//                                TempEvent.DAsset = NewTarget;
//                                DGameModel->Player(DPlayerColor)->AddGameEvent(TempEvent);
//
//                                DPlayerCommands[DPlayerColor].DAction = DCurrentAssetCapability[DPlayerColor];
//                                DPlayerCommands[DPlayerColor].DActors = DSelectedPlayerAssets[DPlayerColor];
//                                DPlayerCommands[DPlayerColor].DTargetColor = PixelType.Color();
//                                DPlayerCommands[DPlayerColor].DTargetType = PixelType.AssetType();
//                                DPlayerCommands[DPlayerColor].DTargetLocation = TempPosition;
//                                DCurrentAssetCapability[DPlayerColor] = actNone;
//                            }
//                        }
//                        else if (((CPlayerCapability::ttTerrain == PlayerCapability->TargetType()) || (CPlayerCapability::ttTerrainOrAsset == PlayerCapability->TargetType())) && ((atNone == PixelType.AssetType()) && (pcNone == PixelType.Color()))) {
//                            auto NewTarget = DGameModel->Player(DPlayerColor)->CreateMarker(TempPosition, false);
//
//                            if (PlayerCapability->CanApply(DSelectedPlayerAssets[DPlayerColor].front().lock(), DGameModel->Player(DPlayerColor), NewTarget)) {
//                                //PlayerCapability->ApplyCapability(DSelectedPlayerAssets[DPlayerColor].front().lock(), DGameModel->Player(DPlayerColor), NewTarget);
//                                SGameEvent TempEvent;
//                                TempEvent.DType = etPlaceAction;
//                                TempEvent.DAsset = NewTarget;
//                                DGameModel->Player(DPlayerColor)->AddGameEvent(TempEvent);
//
//                                DPlayerCommands[DPlayerColor].DAction = DCurrentAssetCapability[DPlayerColor];
//                                DPlayerCommands[DPlayerColor].DActors = DSelectedPlayerAssets[DPlayerColor];
//                                DPlayerCommands[DPlayerColor].DTargetColor = pcNone;
//                                DPlayerCommands[DPlayerColor].DTargetType = atNone;
//                                DPlayerCommands[DPlayerColor].DTargetLocation = TempPosition;
//                                DCurrentAssetCapability[DPlayerColor] = actNone;
//                            }
//                        }
//                        else {
//
//                        }
//
//                    }
//                }
//            }
//        }
//        else if (uictViewportBevelN == ComponentType) {
//            PanningDirection = dNorth;
//            Panning = true;
//        }
//        else if (uictViewportBevelE == ComponentType) {
//            PanningDirection = dEast;
//            Panning = true;
//        }
//        else if (uictViewportBevelS == ComponentType) {
//            PanningDirection = dSouth;
//            Panning = true;
//        }
//        else if (uictViewportBevelW == ComponentType) {
//            PanningDirection = dWest;
//            Panning = true;
//        }
//        else if (uictMiniMap == ComponentType) {
//            if (DLeftClick[DPlayerColor] && !DLeftDown[DPlayerColor]) {
//                CPosition TempPosition(ScreenToMiniMap(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor])));
//                TempPosition = MiniMapToDetailedMap(TempPosition);
//
//                DViewportRenderer->CenterViewport(TempPosition);
//            }
//        }
//        else if (uictUserDescription == ComponentType) {
//            if (DLeftClick[DPlayerColor] && !DLeftDown[DPlayerColor]) {
//                int IconPressed = DUnitDescriptionRenderer->Selection(ScreenToUnitDescription(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor])));
//
//                if (1 == DSelectedPlayerAssets[DPlayerColor].size()) {
//                    if (0 == IconPressed) {
//                        if (auto Asset = DSelectedPlayerAssets[DPlayerColor].front().lock()) {
//                            DViewportRenderer->CenterViewport(Asset->Position());
//                        }
//                    }
//                }
//                else if (0 <= IconPressed) {
//                    while (IconPressed) {
//                        IconPressed--;
//                        DSelectedPlayerAssets[DPlayerColor].pop_front();
//                    }
//                    while (1 < DSelectedPlayerAssets[DPlayerColor].size()) {
//                        DSelectedPlayerAssets[DPlayerColor].pop_back();
//                    }
//                    SGameEvent TempEvent;
//                    TempEvent.DType = etSelection;
//                    TempEvent.DAsset = DSelectedPlayerAssets[DPlayerColor].front().lock();
//                    DGameModel->Player(DPlayerColor)->AddGameEvent(TempEvent);
//                }
//            }
//        }
//        else if (uictUserAction == ComponentType) {
//
//            // Set the Action description to be rendered
//            CursorOverAsset = DUnitActionRenderer->Selection(ScreenToUnitAction(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor])));
//            DGameModel->Player(DPlayerColor)->ActionToRender(CursorOverAsset);
//
//            if (DLeftClick[DPlayerColor] && !DLeftDown[DPlayerColor]) {
//                EAssetCapabilityType CapabilityType = DUnitActionRenderer->Selection(ScreenToUnitAction(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor])));
//
//
//
//                auto PlayerCapability = CPlayerCapability::FindCapability(CapabilityType);
//
//                if (actNone != CapabilityType) {
//                    SGameEvent TempEvent;
//                    TempEvent.DType = etButtonTick;
//                    DGameModel->Player(DPlayerColor)->AddGameEvent(TempEvent);
//                }
//                if (PlayerCapability) {
//                    if ((CPlayerCapability::ttNone == PlayerCapability->TargetType()) || (CPlayerCapability::ttPlayer == PlayerCapability->TargetType())) {
//                        auto ActorTarget = DSelectedPlayerAssets[DPlayerColor].front().lock();
//
//                        if (PlayerCapability->CanApply(ActorTarget, DGameModel->Player(DPlayerColor), ActorTarget)) {
//
//                            DPlayerCommands[DPlayerColor].DAction = CapabilityType;
//                            DPlayerCommands[DPlayerColor].DActors = DSelectedPlayerAssets[DPlayerColor];
//                            DPlayerCommands[DPlayerColor].DTargetColor = pcNone;
//                            DPlayerCommands[DPlayerColor].DTargetType = atNone;
//                            DPlayerCommands[DPlayerColor].DTargetLocation = ActorTarget->Position();
//                            DCurrentAssetCapability[DPlayerColor] = actNone;
//                        }
//                    }
//                    else {
//                        DCurrentAssetCapability[DPlayerColor] = CapabilityType;
//                    }
//                }
//                else {
//                    DCurrentAssetCapability[DPlayerColor] = CapabilityType;
//                }
//            }
//        }
//        else if (uictMenuButton == ComponentType) {
//            DMenuButtonState = DLeftDown[DPlayerColor] ? CButtonRenderer::bsPressed : CButtonRenderer::bsHover;
//            //JPF: Added if Menu button pressed, go to Game Menu
//            if (CButtonRenderer::bsPressed == DMenuButtonState) {
//                ChangeMode(gmGameMenu);
//            }
//        }
//        if (!Panning) {
//            DPanningSpeed = 0;
//        }
//        else {
//            if (dNorth == PanningDirection) {
//                DViewportRenderer->PanNorth(DPanningSpeed>>PAN_SPEED_SHIFT);
//            }
//            else if (dEast == PanningDirection) {
//                DViewportRenderer->PanEast(DPanningSpeed>>PAN_SPEED_SHIFT);
//            }
//            else if (dSouth == PanningDirection) {
//                DViewportRenderer->PanSouth(DPanningSpeed>>PAN_SPEED_SHIFT);
//            }
//            else if (dWest == PanningDirection) {
//                DViewportRenderer->PanWest(DPanningSpeed>>PAN_SPEED_SHIFT);
//            }
//            if (DPanningSpeed) {
//                DPanningSpeed++;
//                if (PAN_SPEED_MAX < DPanningSpeed) {
//                    DPanningSpeed = PAN_SPEED_MAX;
//                }
//            }
//            else {
//                DPanningSpeed = 1<<PAN_SPEED_SHIFT;
//            }
//        }

        DGameModel.Player(DPlayerColor).ActionToRender(CursorOverAsset);

        if (gstSinglePlayer != DGameSessionType) {
//            DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = pcNone;

            // Building packet
            int myColor = DPlayerColor.ordinal();
            int action = DPlayerCommands[DPlayerColor.ordinal()].DAction.ordinal();
            int color = DPlayerCommands[DPlayerColor.ordinal()].DTargetColor.ordinal();
            int type = DPlayerCommands[DPlayerColor.ordinal()].DTargetType.ordinal();
            int x = DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation.X();
            int y = DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation.Y();
            int numActors = DPlayerCommands[DPlayerColor.ordinal()].DActors.size();

            int[] assetIDs;
            int itr = 0;
            if (0 < numActors) {
                assetIDs = new int[numActors];
                ArrayList<CPlayerAsset> playersActiveAssets = DPlayerCommands[DPlayerColor
                        .ordinal()].DActors;
                for (CPlayerAsset activeAsset : playersActiveAssets) {
                    assetIDs[itr] = (activeAsset.ID());
                    itr++;
                }
            } else {
                // If there are no active assets, send a list of one 0
                assetIDs = new int[1];
                assetIDs[itr] = 0;
            }

            DMultiplayer.new SendGameCommand(DMultiplayer.getGame().getHost(), assetIDs, x, y)
                    .execute(myColor, action, color, type);

            UpdateCommands();
        }
    }

    private void UpdateCommands() {
        CMultiplayer.ReceiveCommands receive = DMultiplayer.new ReceiveCommands();
        receive.execute();
        while (receive.getStatus() != AsyncTask.Status.FINISHED);
        ArrayList<Command> commands = receive.cmds;

        if (commands == null) {
            Log.e("UpdateCommands", "Server timeout");
            ChangeMode(gmMainMenu);
        }

        // update commands
        for (int i = 0; i < pcMax.ordinal(); i++) {
            DPlayerCommands[i].DActors.clear();
        }

        for (int i = 0; commands != null && i < commands.size(); i++) {
            Command command = commands.get(i);
            //PrintDebug(DEBUG_LOW, "UpdateCommands: Received command %d\n", i);
            //PrintDebug(DEBUG_LOW, "MyColor: %d, Action: %d, Color: %d, Type: %d, X: %d, Y: %d\n", (int)command->playerColor(), (int)command->action(), (int)command->targetColor(), (int)command->type(), (int)command->x(), (int)command->y());
            DPlayerCommands[command.playerColor()].DAction = EAssetCapabilityType.values()[command
                    .action()];
            DPlayerCommands[command.playerColor()].DTargetColor = EPlayerColor.values()[command
                    .targetColor()];
            DPlayerCommands[command.playerColor()].DTargetType = CGameDataTypes.EAssetType.values()[
                    command.type()];
            DPlayerCommands[command.playerColor()].DTargetLocation.X(command.x());
            DPlayerCommands[command.playerColor()].DTargetLocation.Y(command.y());
            int length = command.assetIdsLength();

            for (int index = 0; index < length; index++) {
                //PrintDebug(DEBUG_LOW, "assetID: %d\n", Ids->Get(index));
                for (CPlayerAsset WeakAsset : DGameModel.Player(CGameDataTypes.EPlayerColor
                        .values()[command.playerColor()]).Assets()) {

//                     if (auto Asset = WeakAsset.lock()) {
                        if (WeakAsset.ID() == command.assetIds(index)) {
                            DPlayerCommands[command.playerColor()].DActors.add(WeakAsset);
                        }
//                    }
                }
            }
        }
    }

    public void ProcessInputGameMode(GameView gameView) {
//
//        boolean Panning = false;
//        boolean ShiftPressed = false;
//        CGameDataTypes.EDirection PanningDirection = dMax;
//
//        for (Long Key : DPressedKeys[DPlayerColor.ordinal()]) {
//            if (GDK_KEY_Up == Key) {
//                PanningDirection = dNorth;
//                Panning = true;
//            } else if (GDK_KEY_Down == Key) {
//                PanningDirection = dSouth;
//                Panning = true;
//            } else if (GDK_KEY_Left == Key) {
//                PanningDirection = dWest;
//                Panning = true;
//            } else if (GDK_KEY_Right == Key) {
//                PanningDirection = dEast;
//                Panning = true;
//            } else if ((GDK_KEY_Shift_L == Key) || (GDK_KEY_Shift_R == Key)) {
//                ShiftPressed = true;
//            }
//        }
//
//        for (Long Key : DReleasedKeys[DPlayerColor.ordinal()]) {
//            // Handle releases
//            if (DSelectedPlayerAssets[DPlayerColor.ordinal()].size()) {
//                boolean CanMove = true;
//                for (CPlayerAsset Asset : DSelectedPlayerAssets[DPlayerColor.ordinal()]) {
//                    if (auto LockedAsset = Asset.lock()) {
//                        if (0 == LockedAsset.Speed()) {
//                            CanMove = false;
//                            break;
//                        }
//                    }
//                }
//                if (GDK_KEY_Escape == Key) {
//                    DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
//                }
//                if (EAssetCapabilityType.actBuildSimple == DCurrentAssetCapability[DPlayerColor.ordinal()]) {
//                    auto KeyLookup = DBuildHotKeyMap.find(Key);
//                    // check build
//                    if (KeyLookup != DBuildHotKeyMap.end()) {
//                        auto PlayerCapability = CPlayerCapability::FindCapability
//                        (KeyLookup.second);
//
//                        if (PlayerCapability) {
//                            auto ActorTarget = DSelectedPlayerAssets[DPlayerColor.ordinal()]
//                                    .front().lock();
//
//                            if (PlayerCapability.CanInitiate(ActorTarget, DGameModel.Player
//                                    (DPlayerColor.ordinal()))) {
//                                SGameEvent TempEvent;
//                                TempEvent.DType = etButtonTick;
//                                DGameModel.Player(DPlayerColor.ordinal()).AddGameEvent(TempEvent);
//
//                                DCurrentAssetCapability[DPlayerColor.ordinal()] = KeyLookup.second;
//                            }
//                        }
//                    } else if (CanMove) {
//                        auto KeyLookup = DUnitHotKeyMap.find(Key);
//
//                        if (KeyLookup != DUnitHotKeyMap.end()) {
//                            boolean HasCapability = true;
//                            for (auto Asset : DSelectedPlayerAssets[DPlayerColor.ordinal()]) {
//                                if (auto LockedAsset = Asset.lock()) {
//                                    if (!LockedAsset.HasCapability(KeyLookup.second)) {
//                                        HasCapability = false;
//                                        break;
//                                    }
//                                }
//                            }
//                            if (HasCapability) {
//                                auto PlayerCapability = CPlayerCapability::FindCapability
//                                (KeyLookup.second);
//                                SGameEvent TempEvent;
//                                TempEvent.DType = etButtonTick;
//                                DGameModel.Player(DPlayerColor.ordinal()).AddGameEvent(TempEvent);
//
//                                if (PlayerCapability) {
//                                    if ((CPlayerCapability::ttNone == PlayerCapability.TargetType
//                                            ()) || (CPlayerCapability::ttPlayer ==
//                                            PlayerCapability.TargetType())) {
//                                        auto ActorTarget = DSelectedPlayerAssets[DPlayerColor.ordinal()]
//                                                .front().lock();
//
//                                        if (PlayerCapability.CanApply(ActorTarget, DGameModel.Player(DPlayerColor), ActorTarget)) {
//
//                                            DPlayerCommands[DPlayerColor.ordinal()].DAction = KeyLookup.second;
//                                            DPlayerCommands[DPlayerColor.ordinal()].DActors =
//                                                    DSelectedPlayerAssets[DPlayerColor];
//                                            DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = pcNone;
//                                            DPlayerCommands[DPlayerColor.ordinal()].DTargetType = atNone;
//                                            DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = ActorTarget
//                                                    .TilePosition();
//                                            DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
//                                        }
//                                    } else {
//                                        DCurrentAssetCapability[DPlayerColor.ordinal()] = KeyLookup.second;
//                                    }
//                                } else {
//                                    DCurrentAssetCapability[DPlayerColor.ordinal()] = KeyLookup.second;
//                                }
//                            }
//                        }
//                    } else {
                        EAssetCapabilityType KeyLookup = actBuildPeasant;//DTrainHotKeyMap.get(Key);
//
        boolean HasCapability = true;
        if (DSelectedPlayerAssets.get(DPlayerColor.ordinal()) != null) {
            for (CPlayerAsset Asset : DSelectedPlayerAssets.get(DPlayerColor.ordinal())) {
                if (!Asset.HasCapability(KeyLookup)) {
                    HasCapability = false;
                    break;
                }
            }
            if (HasCapability) {
                CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability(KeyLookup);
                SGameEvent TempEvent = new SGameEvent(); //TODO
                TempEvent.DType = etButtonTick;
                DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);

                if (PlayerCapability != null) {
                    if ((ETargetType.ttNone == PlayerCapability.TargetType()) || (
                            ETargetType.ttPlayer == PlayerCapability.TargetType())) {
                        if (!DSelectedPlayerAssets.get(DPlayerColor.ordinal()).isEmpty()) {
                            CPlayerAsset ActorTarget = DSelectedPlayerAssets.get(DPlayerColor.ordinal()).get(0);
                            if (PlayerCapability.CanApply(ActorTarget, DGameModel.Player
                                    (DPlayerColor), ActorTarget)) {

                                DPlayerCommands[DPlayerColor.ordinal()].DAction = KeyLookup;
                                DPlayerCommands[DPlayerColor.ordinal()].DActors = DSelectedPlayerAssets.get(DPlayerColor.ordinal());
                                DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = pcNone;
                                DPlayerCommands[DPlayerColor.ordinal()].DTargetType = atNone;
                                DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = ActorTarget.TilePosition();
                                DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
                            } else {
                                DCurrentAssetCapability[DPlayerColor.ordinal()] = KeyLookup;
                            }
                        }
                    }
                }
            } else {
                DCurrentAssetCapability[DPlayerColor.ordinal()] = KeyLookup;
            }
        }

//        DReleasedKeys[pcNone.ordinal()].clear();
//        DReleasedKeys[DPlayerColor.ordinal()].clear();
//
//        DMenuButtonState = CButtonRenderer::bsNone;
//        EUIComponentType ComponentType = FindUIComponentType(CPosition(DCurrentX[DPlayerColor
//                .ordinal()], DCurrentY[DPlayerColor]));
//        if (uictViewport == ComponentType) {
//            CPosition TempPosition
//            (ScreenToDetailedMap(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor])));
        CPosition TempPosition = new CPosition(DCurrentX[DPlayerColor.ordinal()], DCurrentY[DPlayerColor.ordinal()]);
//            CPixelType PixelType = CPixelType.GetPixelType(DViewportTypePixmap, ScreenToViewport(new CPosition(DCurrentX[DPlayerColor.ordinal()], DCurrentY[DPlayerColor.ordinal()])));
//
            CPixelType PixelType = new CPixelType(0,0,0);
            PixelType.DType = CPixelType.EAssetTerrainType.attTownHall; //attPeasant;
            if (DRightClick[DPlayerColor.ordinal()] != 0 && !DRightDown[DPlayerColor.ordinal()]
                    && DSelectedPlayerAssets.get(DPlayerColor.ordinal()).size() != 0) {
//                boolean CanMove = true;
//                for (auto Asset : DSelectedPlayerAssets[DPlayerColor.ordinal()]) {
//                    if (auto LockedAsset = Asset.lock()) {
//                        if (0 == LockedAsset.Speed()) {
//                            CanMove = false;
//                            break;
//                        }
//                    }
//                }
//                if (CanMove) {
//                    if (pcNone != PixelType.Color()) {
//                        // Command is either walk/deliver, repair, or attack
//
//                        DPlayerCommands[DPlayerColor.ordinal()].DAction = actMove;
//                        DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = PixelType.Color();
//                        DPlayerCommands[DPlayerColor.ordinal()].DTargetType = PixelType.AssetType();
//                        DPlayerCommands[DPlayerColor.ordinal()].DActors = DSelectedPlayerAssets[DPlayerColor];
//                        DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = TempPosition;
//                        if (PixelType.Color() == DPlayerColor) {
//                            boolean HaveLumber = false;
//                            boolean HaveGold = false;
//
//                            for (auto Asset : DSelectedPlayerAssets[DPlayerColor.ordinal()]) {
//                                if (auto LockedAsset = Asset.lock()) {
//                                    if (LockedAsset.Lumber()) {
//                                        HaveLumber = true;
//                                    }
//                                    if (LockedAsset.Gold()) {
//                                        HaveGold = true;
//                                    }
//                                }
//                            }
//                            if (HaveGold) {
//                                if ((atTownHall == DPlayerCommands[DPlayerColor.ordinal()]
//                                        .DTargetType) || (atKeep == DPlayerCommands[DPlayerColor
//                                        .ordinal()].DTargetType) || (atCastle ==
//                                        DPlayerCommands[DPlayerColor.ordinal()].DTargetType)) {
//                                    DPlayerCommands[DPlayerColor.ordinal()].DAction = actConvey;
//                                }
//                            } else if (HaveLumber) {
//                                if ((atTownHall == DPlayerCommands[DPlayerColor.ordinal()].DTargetType) || (atKeep ==
//                                        DPlayerCommands[DPlayerColor.ordinal()].DTargetType) ||
//                                        (atCastle == DPlayerCommands[DPlayerColor.ordinal()].DTargetType)
//                                        || (atLumberMill == DPlayerCommands[DPlayerColor.ordinal()]
//                                        .DTargetType)) {
//                                    DPlayerCommands[DPlayerColor.ordinal()].DAction = actConvey;
//                                }
//                            } else {
//                                auto TargetAsset = DGameModel.Player(DPlayerColor.ordinal()).SelectAsset
//                                        (TempPosition, PixelType.AssetType()).lock();
//                                if ((0 == TargetAsset.Speed()) && (TargetAsset.MaxHitPoints() > TargetAsset
//                                        .HitPoints())) {
//                                    DPlayerCommands[DPlayerColor.ordinal()].DAction = actRepair;
//                                }
//                            }
//                        } else {
//                            DPlayerCommands[DPlayerColor.ordinal()].DAction = actAttack;
//                        }
//
//                        DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
//                    } else {
//                        // Command is either walk, mine, harvest
//                        CPosition TempPosition (ScreenToDetailedMap(CPosition(DCurrentX[DPlayerColor
//                                .ordinal()], DCurrentY[DPlayerColor.ordinal()])));
//                        boolean CanHarvest = true;
//
//                        DPlayerCommands[DPlayerColor.ordinal()].DAction = actMove;
//                        DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = pcNone;
//                        DPlayerCommands[DPlayerColor.ordinal()].DTargetType = atNone;
//                        DPlayerCommands[DPlayerColor.ordinal()].DActors =
//                                DSelectedPlayerAssets[DPlayerColor];
//                        DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = TempPosition;
//
//                        for (auto Asset : DSelectedPlayerAssets[DPlayerColor]) {
//                            if (auto LockedAsset = Asset.lock()) {
//                                if (!LockedAsset.HasCapability(actMine)) {
//                                    CanHarvest = false;
//                                    break;
//                                }
//                            }
//                        }
//                        if (CanHarvest) {
//                            if (CPixelType::attTree == PixelType.Type()) {
//                                CPosition TempTilePosition;
//
//                                DPlayerCommands[DPlayerColor.ordinal()].DAction = actMine;
//                                TempTilePosition.SetToTile(DPlayerCommands[DPlayerColor.ordinal()]
//                                        .DTargetLocation);
//                                if (CTerrainMap::ttTree != DGameModel.Player(DPlayerColor.ordinal())
//                                        .PlayerMap().TileType(TempTilePosition)) {
//                                    // Could be tree pixel, but tops of next row
//                                    TempTilePosition.IncrementY(1);
//                                    if (CTerrainMap::ttTree == DGameModel.Player(DPlayerColor)
//                                            .PlayerMap().TileType(TempTilePosition)) {
//                                        DPlayerCommands[DPlayerColor].DTargetLocation.SetFromTile(TempTilePosition);
//                                    }
//                                }
//                            } else if (CPixelType::attGoldMine == PixelType.Type()) {
//                                DPlayerCommands[DPlayerColor.ordinal()].DAction = actMine;
//                                DPlayerCommands[DPlayerColor.ordinal()].DTargetType = atGoldMine;
//                            }
//                        }
//
//                        DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
//                    }
//                }
            } else {//if (DLeftClick[DPlayerColor.ordinal()] != 0) {
//                Log.d("CApplicationData", "enter else");
//                if ((actNone == DCurrentAssetCapability[DPlayerColor.ordinal()]) || (EAssetCapabilityType.actBuildSimple ==
//                        DCurrentAssetCapability[DPlayerColor.ordinal()])) { //selection
//                    Log.d("CApplicationData", "inside if statement");
                    if (DLeftDown[DPlayerColor.ordinal()]) {
                        DMouseDown[DPlayerColor.ordinal()] = TempPosition;
                    } else {
                        SRectangle TempRectangle = new SRectangle();
                        CGameDataTypes.EPlayerColor SearchColor = DPlayerColor;
//                        Log.d("chicken", "SearchColor: " + DPlayerColor);
                        ArrayList<CPlayerAsset> PreviousSelections = new ArrayList<>();

                        if (DSelectedPlayerAssets.get(DPlayerColor.ordinal()) != null) {
                            for (CPlayerAsset WeakAsset : DSelectedPlayerAssets.get(DPlayerColor
                                    .ordinal())) {
//                            if (auto LockedAsset = WeakAsset.lock()) {
                                PreviousSelections.add(WeakAsset);
//                            }
                            }
                        }

//                        Log.d("CApplicationData", "temposition x: " + TempPosition.X() + "y: " + TempPosition.Y());


                        TempRectangle.DXPosition = (int)gameView.firstX;//Math.min((int)gameView.firstX, TempPosition.X());
                        TempRectangle.DYPosition = (int)gameView.firstY;//Math.min((int)gameView.firstY, TempPosition.Y());
//                        TempRectangle.DWidth = Math.max((int)gameView.firstX,
//                                TempPosition.X()) - TempRectangle.DXPosition;
//                        TempRectangle.DHeight = Math.max((int)gameView.firstY,
//                                TempPosition.Y()) - TempRectangle.DYPosition;
                        TempRectangle.DWidth = 0;
                        TempRectangle.DHeight = 0;

                        if ((TempRectangle.DWidth < CPosition.TileWidth()) ||
                                (TempRectangle.DHeight < CPosition.TileHeight()) ||
                                (2 == DLeftClick[DPlayerColor.ordinal()])) {
                            TempRectangle.DXPosition = (int)gameView.firstX;//TempPosition.X();
                            TempRectangle.DYPosition = (int)gameView.firstX; //TempPosition.Y();
                            TempRectangle.DWidth = 0;
                            TempRectangle.DHeight = 0;
//                            SearchColor = PixelType.Color();
//                            Log.d("chicken", "SearchColor2 " + SearchColor);
//                            SearchColor = EPlayerColor.pcBlack;
                        }

                        if (SearchColor != DPlayerColor) {
                            if (DSelectedPlayerAssets.get(DPlayerColor.ordinal()) != null) {
                                DSelectedPlayerAssets.get(DPlayerColor.ordinal()).clear();
                            }
                        }
//                        if (ShiftPressed) {
//                            if (!DSelectedPlayerAssets[DPlayerColor.ordinal()].empty()) {
//                                if (auto TempAsset = DSelectedPlayerAssets[DPlayerColor.ordinal()]
//                                        .front().lock()) {
//                                    if (TempAsset.Color() != DPlayerColor) {
//                                        DSelectedPlayerAssets[DPlayerColor.ordinal()].clear();
//                                    }
//                                }
//                            }
//                            DSelectedPlayerAssets[DPlayerColor.ordinal()].splice
//                                    (DSelectedPlayerAssets[DPlayerColor.ordinal()].end(), DGameModel
//                                            .Player(SearchColor).SelectAssets(TempRectangle,
//                                                    PixelType.AssetType(), 2 ==
//                                                            DLeftClick[DPlayerColor.ordinal()]));
//                            DSelectedPlayerAssets[DPlayerColor.ordinal()].sort(WeakPtrCompare < CPlayerAsset >);
//                            DSelectedPlayerAssets[DPlayerColor.ordinal()].unique(WeakPtrEquals < CPlayerAsset >);
//                        } else {
                            PreviousSelections.clear();
                            DSelectedPlayerAssets.add(DPlayerColor.ordinal(), DGameModel.Player(SearchColor).SelectAssets(TempRectangle, PixelType.AssetType(), 2 == DLeftClick[DPlayerColor.ordinal()]));
//                        Log.d("CApplicationData", "arr: " + DGameModel.Player(SearchColor).SelectAssets(TempRectangle, PixelType.AssetType(), 2 == DLeftClick[DPlayerColor.ordinal()]));
//                        }
                        for (CPlayerAsset WeakAsset : DSelectedPlayerAssets.get(DPlayerColor
                                .ordinal())) {
//                            if (auto LockedAsset = WeakAsset.lock()) {
                            boolean FoundPrevious = false;
                                for (CPlayerAsset PrevAsset : PreviousSelections) {
                                    if (PrevAsset == WeakAsset) {
                                        FoundPrevious = true;
                                        break;
                                    }
                                }
                                if (!FoundPrevious) {
                                    SGameEvent TempEvent = new SGameEvent();
                                    TempEvent.DType = etSelection;
                                    TempEvent.DAsset = WeakAsset;
                                    DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);
                                }
//                            }
                        }

                        DMouseDown[DPlayerColor.ordinal()] = new CPosition(-1, -1);
                    }
                    DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
//                }
//                } else { //for commands besides buildsimple
//                    auto PlayerCapability = CPlayerCapability.FindCapability
//                    (DCurrentAssetCapability[DPlayerColor.ordinal()]);
//
//                    if (PlayerCapability && !DLeftDown[DPlayerColor.ordinal()]) {
//                        if (((CPlayerCapability.ttAsset == PlayerCapability.TargetType()) ||
//                                (CPlayerCapability.ttTerrainOrAsset == PlayerCapability
//                                        .TargetType())) && (atNone != PixelType.AssetType())) {
//                            auto NewTarget = DGameModel.Player(PixelType.Color()).SelectAsset
//                                    (TempPosition, PixelType.AssetType()).lock();
//
//                            if (PlayerCapability.CanApply(DSelectedPlayerAssets[DPlayerColor]
//                                    .front().lock(), DGameModel.Player
//                                    (DPlayerColor), NewTarget)) {
//                                //PlayerCapability.ApplyCapability(DSelectedPlayerAssets[DPlayerColor].front().lock(), DGameModel.Player
//                                // (DPlayerColor), NewTarget);
//                                SGameEvent TempEvent;
//                                TempEvent.DType = etPlaceAction;
//                                TempEvent.DAsset = NewTarget;
//                                DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);
//
//                                DPlayerCommands[DPlayerColor.ordinal()].DAction =
//                                        DCurrentAssetCapability[DPlayerColor.ordinal()];
//                                DPlayerCommands[DPlayerColor.ordinal()].DActors =
//                                        DSelectedPlayerAssets[DPlayerColor.ordinal()];
//                                DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = PixelType.Color();
//                                DPlayerCommands[DPlayerColor.ordinal()].DTargetType = PixelType
//                                        .AssetType();
//                                DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = TempPosition;
//                                DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
//                            }
//                        } else if (((CPlayerCapability.ttTerrain == PlayerCapability.TargetType()
//                        ) || (CPlayerCapability.ttTerrainOrAsset == PlayerCapability.TargetType
//                                ())) && ((atNone == PixelType.AssetType()) &&
//                                (pcNone == PixelType.Color()))) {
//                            auto NewTarget = DGameModel.Player(DPlayerColor).CreateMarker(TempPosition, false);
//
//                            if (PlayerCapability.CanApply(DSelectedPlayerAssets[DPlayerColor].front().lock(), DGameModel.Player(DPlayerColor), NewTarget)) {
//                                //PlayerCapability.ApplyCapability(DSelectedPlayerAssets[DPlayerColor].front().lock(), DGameModel.Player(DPlayerColor), NewTarget);
//                                SGameEvent TempEvent;
//                                TempEvent.DType = etPlaceAction;
//                                TempEvent.DAsset = NewTarget;
//                                DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);
//
//                                DPlayerCommands[DPlayerColor.ordinal()].DAction =
//                                        DCurrentAssetCapability[DPlayerColor.ordinal()];
//                                DPlayerCommands[DPlayerColor.ordinal()].DActors =
//                                        DSelectedPlayerAssets[DPlayerColor.ordinal()];
//                                DPlayerCommands[DPlayerColor.ordinal()].DTargetColor = pcNone;
//                                DPlayerCommands[DPlayerColor.ordinal()].DTargetType = atNone;
//                                DPlayerCommands[DPlayerColor.ordinal()].DTargetLocation = TempPosition;
//                                DCurrentAssetCapability[DPlayerColor.ordinal()] = actNone;
//                            }
//                        }
//                    }
//                }
            }
//        } else if (uictViewportBevelN == ComponentType) {
//            PanningDirection = dNorth;
//            Panning = true;
//        } else if (uictViewportBevelE == ComponentType) {
//            PanningDirection = dEast;
//            Panning = true;
//        } else if (uictViewportBevelS == ComponentType) {
//            PanningDirection = dSouth;
//            Panning = true;
//        } else if (uictViewportBevelW == ComponentType) {
//            PanningDirection = dWest;
//            Panning = true;
//        } else if (uictMiniMap == ComponentType) {
//            if (DLeftClick[DPlayerColor.ordinal()] != 0 && !DLeftDown[DPlayerColor.ordinal()]) {
//                CPosition TempPosition
//                (ScreenToMiniMap(CPosition(DCurrentX[DPlayerColor], DCurrentY[DPlayerColor])));
//                TempPosition = MiniMapToDetailedMap(TempPosition);
//
//                DViewportRenderer.CenterViewport(TempPosition);
//            }
//        } else if (uictUserDescription == ComponentType) {
//            if (DLeftClick[DPlayerColor.ordinal()] && !DLeftDown[DPlayerColor]) {
//                int IconPressed = DUnitDescriptionRenderer.Selection(ScreenToUnitDescription(CPosition(DCurrentX[DPlayerColor],
//                        DCurrentY[DPlayerColor])));
//
//                if (1 == DSelectedPlayerAssets[DPlayerColor].size()) {
//                    if (0 == IconPressed) {
//                        if (auto Asset = DSelectedPlayerAssets[DPlayerColor].front().lock()) {
//                            DViewportRenderer.CenterViewport(Asset.Position());
//                        }
//                    }
//                } else if (0 <= IconPressed) {
//                    while (IconPressed != 0) {
//                        IconPressed--;
//                        DSelectedPlayerAssets[DPlayerColor].pop_front();
//                    }
//                    while (1 < DSelectedPlayerAssets[DPlayerColor].size()) {
//                        DSelectedPlayerAssets[DPlayerColor].pop_back();
//                    }
//                    SGameEvent TempEvent;
//                    TempEvent.DType = etSelection;
//                    TempEvent.DAsset = DSelectedPlayerAssets[DPlayerColor.ordinal()].front().lock();
//                    DGameModel.Player(DPlayerColor.ordinal()).AddGameEvent(TempEvent);
//                }
//            }
//        } else if (uictUserAction == ComponentType) {
//            if (DLeftClick[DPlayerColor.ordinal()] != 0 && !DLeftDown[DPlayerColor.ordinal()]) {
//                CGameDataTypes.EAssetCapabilityType CapabilityType = DUnitActionRenderer.Selection
//                        (ScreenToUnitAction(new CPosition(DCurrentX[DPlayerColor.ordinal()],
//                                DCurrentY[DPlayerColor.ordinal()])));
//                auto PlayerCapability = CPlayerCapability::FindCapability (CapabilityType);
//                if (actNone != CapabilityType) {
//                    SGameEvent TempEvent;
//                    TempEvent.DType = etButtonTick;
//                    DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);
//                }
//                if (PlayerCapability) {
//                    if ((CPlayerCapability::ttNone == PlayerCapability.TargetType()) ||
//                            (CPlayerCapability::ttPlayer == PlayerCapability.TargetType())) {
//
//                        auto ActorTarget = DSelectedPlayerAssets[DPlayerColor.ordinal()].front()
//                                .lock();
//
//                        if (PlayerCapability.CanApply(ActorTarget, DGameModel.Player(DPlayerColor),
//                                ActorTarget)) {
//
//                            DPlayerCommands[DPlayerColor.ordinal()].DAction = CapabilityType;
//                            DPlayerCommands[DPlayerColor.ordinal()].DActors =
//                                    DSelectedPlayerAssets[DPlayerColor.ordinal()];
//                            DPlayerCommands[DPlayerColor].DTargetColor = pcNone;
//                            DPlayerCommands[DPlayerColor].DTargetType = atNone;
//                            DPlayerCommands[DPlayerColor].DTargetLocation = ActorTarget.Position();
//                            DCurrentAssetCapability[DPlayerColor] = actNone;
//                        }
//                    } else {
//                        DCurrentAssetCapability[DPlayerColor.ordinal()] = CapabilityType;
//                    }
//                } else {
//                    DCurrentAssetCapability[DPlayerColor.ordinal()] = CapabilityType;
//                }
//            }
//        } else if (uictMenuButton == ComponentType) {
//            DMenuButtonState = DLeftDown[DPlayerColor.ordinal()] ? CButtonRenderer::bsPressed :
//                    CButtonRenderer::bsHover;
//            //JPF: Added if Menu button pressed, go to Game Menu
//            if (CButtonRenderer::bsPressed == DMenuButtonState) {
//                ChangeMode(gmGameMenu);
//            }
//        }
//        if (!Panning) {
//            DPanningSpeed = 0;
//        } else {
//            if (dNorth.ordinal() == PanningDirection) {
//                DViewportRenderer.PanNorth(DPanningSpeed >> PAN_SPEED_SHIFT);
//            } else if (dEast == PanningDirection) {
//                DViewportRenderer.PanEast(DPanningSpeed >> PAN_SPEED_SHIFT);
//            } else if (dSouth == PanningDirection) {
//                DViewportRenderer.PanSouth(DPanningSpeed >> PAN_SPEED_SHIFT);
//            } else if (dWest == PanningDirection) {
//                DViewportRenderer.PanWest(DPanningSpeed >> PAN_SPEED_SHIFT);
//            }
//
//            if (DPanningSpeed != 0) {
//                DPanningSpeed++;
//                if (PAN_SPEED_MAX < DPanningSpeed) {
//                    DPanningSpeed = PAN_SPEED_MAX;
//                }
//            } else {
//                DPanningSpeed = 1 << PAN_SPEED_SHIFT;
//            }
//        }
    }

//    // Checks if 1 player left and qiuts to main menu
//    //TODO refactor into function
    private void CheckEndGame() {
        int PlayersLeft = 0;
        for (int Index = 1; Index < pcMax.ordinal(); Index++) {
            if (DGameModel.Player(CGameDataTypes.EPlayerColor.values()[Index]).IsAlive()) {
                if (DGameModel.Player(CGameDataTypes.EPlayerColor.values()[Index]).Assets().size
                        () > 0) {
                    PlayersLeft++;
                }
            }
        }
        if (PlayersLeft == 1) {
            ChangeMode(gmGameOver); //ChangeMode(gmMainMenu);
        }
    }

    /**
     * Part 2 of Gamelogic
     * Calls TimeStep().
     **/

    public void CalculateGameMode() {
        // Process AI commands
        for (int Index = 1; Index < pcMax.ordinal(); Index++) {
            if (DGameModel.Player(CGameDataTypes.EPlayerColor.values()[Index]).IsAlive() &&
                    DGameModel.Player(CGameDataTypes.EPlayerColor.values()[Index]).IsAI()) {
                DAIPlayers.get(Index).CalculateCommand(DPlayerCommands[Index]);
            }
        }

    // Checks if 1 player left and quits to main menu
    // EL: Maybe consider adding this to CalculateGameMode

//        for (int Index = 1; Index < pcMax.ordinal(); Index++) {
//            if (DGameModel.Player(EPlayerColor.values()[Index]).IsAlive() != 0 && (ptHuman !=
//                    DLoadingPlayerTypes[Index])) {
//                //PrintDebug(DEBUG_LOW, "Calculating AI command\n");
//                DAIPlayers[Index].CalculateCommand(DPlayerCommands[Index]);
//            }
//        }

        for (int Index = 1; Index < pcMax.ordinal(); Index++) {
            if (DPlayerCommands[Index] != null && actNone != DPlayerCommands[Index].DAction) {
                CPlayerCapability PlayerCapability = CPlayerCapability.FindCapability
                        (DPlayerCommands[Index].DAction);
                if (PlayerCapability != null) {
                    CPlayerAsset NewTarget = new CPlayerAsset();
                    if ((ETargetType.ttNone != PlayerCapability.TargetType()) && (ETargetType
                            .ttPlayer != PlayerCapability.TargetType())) {
                        if (atNone == DPlayerCommands[Index].DTargetType) {
                            NewTarget = DGameModel.Player(EPlayerColor.values()[Index])
                                    .CreateMarker(DPlayerCommands[Index].DTargetLocation, true);
                        } else {
                            NewTarget = DGameModel.Player(DPlayerCommands[Index].DTargetColor)
                                    .SelectAsset(DPlayerCommands[Index].DTargetLocation,
                                            DPlayerCommands[Index].DTargetType);
                        }
                    }

                    for (CPlayerAsset WeakActor : DPlayerCommands[Index].DActors) {
                        if (PlayerCapability.CanApply(WeakActor, DGameModel.Player(EPlayerColor
                                .values()[Index]), NewTarget) && (WeakActor.Interruptible() ||
                                (EAssetCapabilityType.actCancel == DPlayerCommands[Index]
                                        .DAction))) {
                            PlayerCapability.ApplyCapability(WeakActor, DGameModel.Player
                                    (EPlayerColor.values()[Index]), NewTarget);
                        }
                    }
                }
                DPlayerCommands[Index].DAction = actNone;

                // Deselect units

                if (DSelectedPlayerAssets.get(Index) != null && !DSelectedPlayerAssets.get(Index)
                        .isEmpty()) {
                    DSelectedPlayerAssets.get(Index).clear();
                }
            }
        }
        DGameModel.TimeStep();

        CheckEndGame();

//        for (CPlayerAsset Asset : DSelectedPlayerAssets.get(DPlayerColor.ordinal())) {
//            if (DGameModel.ValidAsset(Asset) && Asset.Alive()) {
//                if (Asset.Speed() > 0 && (aaCapability == Asset.Action())) {
//                    SAssetCommand Command = Asset.CurrentCommand();
//
//                    if (Command.DAssetTarget != null && (aaConstruct == Command.DAssetTarget
//                            .Action())) {
//                        SGameEvent TempEvent = new SGameEvent();
//
//                        DSelectedPlayerAssets.get(DPlayerColor.ordinal()).clear();
//                        DSelectedPlayerAssets.get(DPlayerColor.ordinal()).add(Command.DAssetTarget);
//
//                        TempEvent.DType = etSelection;
//                        TempEvent.DAsset = Command.DAssetTarget;
//                        DGameModel.Player(DPlayerColor).AddGameEvent(TempEvent);
//                        break;
//                    }
//                }
//            } else {
//                DSelectedPlayerAssets.get(DPlayerColor.ordinal()).remove(Asset);
//            }
//        }
    }

    /**
     * Called by GameView's draw() method
     */
    public void RenderGameMap(Canvas canvas) {
        SRectangle TempRectangle = new SRectangle(0, 0, 0, 0);
        ArrayList<CPlayerAsset> SelectedAndMarkerAssets = DSelectedPlayerAssets.get(DPlayerColor.ordinal());
        DViewportRenderer.DrawViewport(canvas, SelectedAndMarkerAssets, TempRectangle, DCurrentAssetCapability[DPlayerColor.ordinal()]);
    }

    /**
     * Called by SideBarView's draw() method
     */
    public void RenderSideBar(Canvas canvas) {
        DUnitDescriptionRenderer.UpdateDescriptions(DSelectedPlayerAssets.get(DPlayerColor.ordinal()));
        DMapRenderer.DrawMiniMap(canvas);
        DAssetRenderer.DrawMiniAssets(canvas, DMapRenderer.getScale());
        DMapRenderer.DrawVisionRect(canvas);
    }

    /**
     * Part 3 of Game logic
     * Render game objects in map (graphics)
     * Renders the bevel, resources, notifications, and minimap.
     */
    void RenderGameMode() {
        int BufferWidth, BufferHeight;
        int ViewWidth, ViewHeight;
        int MiniMapWidth, MiniMapHeight;
        int DescriptionWidth, DescriptionHeight;
        int ActionWidth, ActionHeight;
        int ResourceWidth, ResourceHeight;

        if (DGameView == null) {
            return;
        }
        DGameView.invalidate();

        if (DSideBarView == null) {
            return;
        }
        DSideBarView.invalidate();

        DMainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Resources
                if (DTypeface == null) {
                    DTypeface = Typeface.createFromAsset(DMainActivity.getAssets(), "Kingthings_Exeter.ttf");
                }
                TextView tvGold = (TextView) DMainActivity.findViewById(R.id.tv_gold);
                TextView tvLumber = (TextView) DMainActivity.findViewById(R.id.tv_lumber);
                TextView tvFood = (TextView) DMainActivity.findViewById(R.id.tv_food);

                tvGold.setText(String.valueOf(DApplicationData.DGameModel.Player(DApplicationData.DPlayerColor).Gold()));
                tvLumber.setText(String.valueOf(DApplicationData.DGameModel.Player(DApplicationData.DPlayerColor).Lumber()));
                tvFood.setText(String.valueOf(DApplicationData.DGameModel.Player(DApplicationData.DPlayerColor).FoodConsumption()) + "/" + String.valueOf(
                        DApplicationData.DGameModel.Player(DApplicationData.DPlayerColor)
                                .FoodProduction()));

                tvFood.setTypeface(DTypeface);
                tvGold.setTypeface(DTypeface);
                tvLumber.setTypeface(DTypeface);

                // Information
            }
        });

//        ViewWidth = DViewportPixmap.getWidth();
//        ViewHeight = DViewportPixmap.getHeight();
//        MiniMapWidth = DMiniMapPixmap.getWidth();
//        MiniMapHeight = DMiniMapPixmap.getHeight();
//        gdk_pixmap_get_size(DWorkingBufferPixmap, BufferWidth, BufferHeight);
//        gdk_pixmap_get_size(DUnitDescriptionPixmap, DescriptionWidth, DescriptionHeight);
//        gdk_pixmap_get_size(DUnitActionPixmap, ActionWidth, ActionHeight);
//        gdk_pixmap_get_size(DResourcePixmap, ResourceWidth, ResourceHeight);
//
//        if (DLeftDown[DPlayerColor.ordinal()] && 0 < DMouseDown[DPlayerColor.ordinal()].X()) {
//            CPosition TempPosition (ScreenToDetailedMap(new CPosition(DCurrentX[DPlayerColor
//                    .ordinal()], DCurrentY[DPlayerColor.ordinal()])));
//            TempRectangle.DXPosition = MIN(DMouseDown[DPlayerColor.ordinal()].X(), TempPosition.X());
//            TempRectangle.DYPosition = MIN(DMouseDown[DPlayerColor.ordinal()].Y(), TempPosition.Y());
//            TempRectangle.DWidth = MAX(DMouseDown[DPlayerColor.ordinal()].X(), TempPosition.X()) -
//                    TempRectangle.DXPosition;
//            TempRectangle.DHeight = MAX(DMouseDown[DPlayerColor.ordinal()].Y(), TempPosition.Y()) -
//                    TempRectangle.DYPosition;
//        } else {
//            CPosition TempPosition = new CPosition(ScreenToDetailedMap(new CPosition
//                    (DCurrentX[DPlayerColor.ordinal()], DCurrentY[DPlayerColor.ordinal()])));
//            TempRectangle.DXPosition = TempPosition.X();
//            TempRectangle.DYPosition = TempPosition.Y();
//        }
//
//        DBackgroundTileset.DrawTileRectangle(DWorkingBufferPixmap, 0, 0, BufferWidth, BufferHeight, 0);
//        DInnerBevel.DrawBevel(DWorkingBufferPixmap, DViewportXOffset, DViewportYOffset, ViewWidth, ViewHeight);
//        DInnerBevel.DrawBevel(DWorkingBufferPixmap, DMiniMapXOffset, DMiniMapYOffset, MiniMapWidth, MiniMapHeight);
//
//        gdk_draw_pixmap(DResourcePixmap, DDrawingContext, DWorkingBufferPixmap, DViewportXOffset, 0, 0, 0, ResourceWidth, ResourceHeight);
//        DResourceRenderer.DrawResources(DResourcePixmap, DDrawingContext);
//        gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DResourcePixmap, 0, 0, DViewportXOffset, 0, -1, -1);
//
//        DOuterBevel.DrawBevel(DWorkingBufferPixmap, DUnitDescriptionXOffset, DUnitDescriptionYOffset, DescriptionWidth, DescriptionHeight);
//        gdk_draw_pixmap(DUnitDescriptionPixmap, DDrawingContext, DWorkingBufferPixmap, DUnitDescriptionXOffset, DUnitDescriptionYOffset, 0, 0, DescriptionWidth, DescriptionHeight);
//        DUnitDescriptionRenderer.DrawUnitDescription(DUnitDescriptionPixmap, DSelectedPlayerAssets[DPlayerColor]);
//        gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DUnitDescriptionPixmap, 0, 0, DUnitDescriptionXOffset, DUnitDescriptionYOffset, -1, -1);
//
//        DOuterBevel.DrawBevel(DWorkingBufferPixmap, DUnitActionXOffset, DUnitActionYOffset, ActionWidth, ActionHeight);
//        gdk_draw_pixmap(DUnitActionPixmap, DDrawingContext, DWorkingBufferPixmap, DUnitActionXOffset, DUnitActionYOffset, 0, 0, ActionWidth, ActionHeight);
//        DUnitActionRenderer.DrawUnitAction(DUnitActionPixmap, DSelectedPlayerAssets[DPlayerColor], DCurrentAssetCapability[DPlayerColor]);
//        gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DUnitActionPixmap, 0, 0, DUnitActionXOffset, DUnitActionYOffset, -1, -1);
//
//        for (auto Asset : DGameModel.Player(DPlayerColor).PlayerMap().Assets()) {
//            if (atNone == Asset.Type()) {
//                SelectedAndMarkerAssets.add(Asset);
//            }
//        }
//        DViewportRenderer.DrawViewport(DViewportPixmap, DViewportTypePixmap, SelectedAndMarkerAssets, TempRectangle, DCurrentAssetCapability[DPlayerColor]);
//        DMiniMapRenderer.DrawMiniMap(DMiniMapPixmap);
//
//        gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DMiniMapPixmap, 0, 0, DMiniMapXOffset, DMiniMapYOffset, -1, -1);
//        gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DViewportPixmap, 0, 0, DViewportXOffset, DViewportYOffset, -1, -1);
//
//
//        DMenuButtonRenderer.DrawButton(DWorkingBufferPixmap, DMenuButtonXOffset, DMenuButtonYOffset, DMenuButtonState);
//
//        switch (FindUIComponentType(new CPosition(DCurrentX[DPlayerColor.ordinal()],
//                DCurrentY[DPlayerColor.ordinal()]))) {
//            case uictViewport: {
//                CPosition ViewportCursorLocation = ScreenToViewport(new CPosition
//                        (DCurrentX[DPlayerColor.ordinal()], DCurrentY[DPlayerColor.ordinal()]));
//                CPixelType PixelType = CPixelType::GetPixelType (DViewportTypePixmap,
//                        ViewportCursorLocation.X(), ViewportCursorLocation.Y());
//                DCursorType = ctPointer;
//                if (actNone == DCurrentAssetCapability[DPlayerColor]) {
//                    if (PixelType.Color() == DPlayerColor) {
//                        DCursorType = ctInspect;
//                    }
//                } else {
//                    auto PlayerCapability = CPlayerCapability::FindCapability
//                    (DCurrentAssetCapability[DPlayerColor.ordinal()]);
//
//                    if (PlayerCapability) {
//                        boolean CanApply = false;
//
//                        if (atNone == PixelType.AssetType()) {
//                            if ((CPlayerCapability::ttTerrain == PlayerCapability.TargetType()) ||
//                                    (CPlayerCapability::ttTerrainOrAsset == PlayerCapability.TargetType()
//                                    )) {
//                                auto NewTarget = DGameModel.Player(DPlayerColor).CreateMarker
//                                        (ViewportToDetailedMap(ViewportCursorLocation), false);
//
//                                CanApply = PlayerCapability.CanApply(DSelectedPlayerAssets[DPlayerColor].front().lock(), DGameModel.Player
//                                        (DPlayerColor), NewTarget);
//                            }
//                        } else {
//                            if ((CPlayerCapability::ttAsset == PlayerCapability.TargetType()) ||
//                                    (CPlayerCapability::ttTerrainOrAsset == PlayerCapability.TargetType())) {
//                                auto NewTarget = DGameModel.Player(PixelType.Color()).SelectAsset(ViewportToDetailedMap
//                                        (ViewportCursorLocation), PixelType.AssetType()).lock();
//
//                                CanApply = PlayerCapability.CanApply(DSelectedPlayerAssets[DPlayerColor].front().lock(), DGameModel.Player
//                                        (DPlayerColor), NewTarget);
//                            }
//                        }
//
//                        DCursorType = CanApply ? ctTargetOn : ctTargetOff;
//                    }
//                }
//            }
//            break;
//            case uictViewportBevelN:
//                DCursorType = ctArrowN;
//                break;
//            case uictViewportBevelE:
//                DCursorType = ctArrowE;
//                break;
//            case uictViewportBevelS:
//                DCursorType = ctArrowS;
//                break;
//            case uictViewportBevelW:
//                DCursorType = ctArrowW;
//                break;
//            default:
//                DCursorType = ctPointer;
//                break;

        SRectangle ViewportRectangle = new SRectangle(DViewportRenderer.ViewportX(),
                DViewportRenderer.ViewportY(), DViewportRenderer.LastViewportWidth(),
                DViewportRenderer.LastViewportHeight());

        DSoundEventRenderer.RenderEvents(ViewportRectangle);
    }

    private void ProcessInputButtonMenuMode() {
//        if (DLeftClick[pcNone.ordinal()] != 0 && !DLeftDown[pcNone.ordinal()]) {
//            for (int Index = 0; Index < DCurrentPageButtonLocations.size(); Index++) {
//                if ((DCurrentPageButtonLocations[Index].DXPosition <= DCurrentX[pcNone]) && ((DCurrentPageButtonLocations[Index].DXPosition + DCurrentPageButtonLocations[Index].DWidth > DCurrentX[pcNone]))) {
//                    if ((DCurrentPageButtonLocations[Index].DYPosition <= DCurrentY[pcNone]) && ((DCurrentPageButtonLocations[Index].DYPosition + DCurrentPageButtonLocations[Index].DHeight > DCurrentY[pcNone]))) {
//                        DCurrentPageButtonsFunctions[Index](this);
//                    }
//                }
//            }
//        }
    }

    private void CalculateButtonMenuMode() {

    }

    void ProcessInputEditOptionsMode() {
//        if (DLeftClick[pcNone.ordinal()] != 0 && !DLeftDown[pcNone.ordinal()]) {
//            boolean ClickedEdit = false;
//            for (int Index = 0; Index < DCurrentPageButtonLocations.size(); Index++) {
//                if ((DCurrentPageButtonLocations[Index].DXPosition <= DCurrentX[pcNone.ordinal()]) && ((DCurrentPageButtonLocations[Index]
//                        .DXPosition + DCurrentPageButtonLocations[Index].DWidth > DCurrentX[pcNone.ordinal()]))) {
//                    if ((DCurrentPageButtonLocations[Index].DYPosition <= DCurrentY[pcNone.ordinal()]) && (
//                            (DCurrentPageButtonLocations[Index].DYPosition + DCurrentPageButtonLocations[Index].DHeight >
//                                    DCurrentY[pcNone.ordinal()]))) {
//                        DCurrentPageButtonsFunctions[Index] (this);
//                    }
//                }
//            }
//            for (int Index = 0; Index < DOptionsEditLocations.size(); Index++) {
//                if ((DOptionsEditLocations[Index].DXPosition <= DCurrentX[pcNone.ordinal()]) && ((DOptionsEditLocations[Index].DXPosition + DOptionsEditLocations[Index].DWidth > DCurrentX[pcNone.ordinal()]))) {
//                    if ((DOptionsEditLocations[Index].DYPosition <= DCurrentY[pcNone.ordinal()]) && ((DOptionsEditLocations[Index].DYPosition + DOptionsEditLocations[Index].DHeight > DCurrentY[pcNone.ordinal()]))) {
//                        if (Index != DOptionsEditSelected) {
//                            DOptionsEditSelected = Index;
//                            DOptionsEditSelectedCharacter = DOptionsEditText[Index].size();
//                            ClickedEdit = true;
//                        }
//                    }
//                }
//            }
//            if (!ClickedEdit) {
//                DOptionsEditSelected = -1;
//            }
//        }
//        for (Long Key : DReleasedKeys[pcNone.ordinal()]) {
//            if (GDK_KEY_Escape == Key) {
//                DOptionsEditSelected = -1;
//            } else if (0 <= DOptionsEditSelected) {
//                if ((GDK_KEY_Delete == Key) || (GDK_KEY_BackSpace == Key)) {
//                    if (DOptionsEditSelectedCharacter) {
//                        DOptionsEditText[DOptionsEditSelected] = DOptionsEditText[DOptionsEditSelected].substr(0, DOptionsEditSelectedCharacter - 1) + DOptionsEditText[DOptionsEditSelected].substr(DOptionsEditSelectedCharacter, DOptionsEditText[DOptionsEditSelected].length() - DOptionsEditSelectedCharacter);
//                        DOptionsEditSelectedCharacter--;
//                    } else if (DOptionsEditText[DOptionsEditSelected].length()) {
//                        DOptionsEditText[DOptionsEditSelected] = DOptionsEditText[DOptionsEditSelected].substr(1);
//                    }
//                } else if (GDK_KEY_Left == Key) {
//                    if (DOptionsEditSelectedCharacter) {
//                        DOptionsEditSelectedCharacter--;
//                    }
//                } else if (GDK_KEY_Right == Key) {
//                    if (DOptionsEditSelectedCharacter < DOptionsEditText[DOptionsEditSelected].length()) {
//                        DOptionsEditSelectedCharacter++;
//                    }
//                } else if (((GDK_KEY_0 <= Key) && (GDK_KEY_9 >= Key)) || ((GDK_KEY_A <= Key) && (GDK_KEY_Z >= Key)) || ((GDK_KEY_a <= Key) && (GDK_KEY_z >= Key)) || (GDK_KEY_period == Key)) {
//                    DOptionsEditText[DOptionsEditSelected] = DOptionsEditText[DOptionsEditSelected].substr(0, DOptionsEditSelectedCharacter) + String(1, (char) Key) + DOptionsEditText[DOptionsEditSelected].substr(DOptionsEditSelectedCharacter, DOptionsEditText[DOptionsEditSelected].length() - DOptionsEditSelectedCharacter);
//                    DOptionsEditSelectedCharacter++;
//                }
//            }
//        }
//        DReleasedKeys[pcNone.ordinal()].clear();
//        DReleasedKeys[DPlayerColor.ordinal()].clear();
    }

    void CalculateEditOptionsMode() {

    }

    void RenderEditOptionsMode() {
//        int BufferWidth, BufferHeight, BufferCenter;
//        int TitleHeight, OptionSkip, OptionTop, TextOffsetY;
//        int ButtonLeft, ButtonSkip, ButtonTop;
//        boolean ButtonXAlign = false, ButtonHovered = false;
//        boolean FirstButton = true, AllInputsValid = true;
//        int GoldColor, WhiteColor, ShadowColor;
//
//
//        RenderMenuTitle(DCurrentPageTitle, TitleHeight, BufferWidth, BufferHeight);
//
//
//        GoldColor = DFonts[CUnitDescriptionRenderer::fsLarge].FindPixel("gold");
//        WhiteColor = DFonts[CUnitDescriptionRenderer::fsLarge].FindPixel("white");
//        ShadowColor = DFonts[CUnitDescriptionRenderer::fsLarge].FindPixel("black");
//
//        DCurrentPageButtonLocations.clear();
//        for (String Text : DCurrentPageButtonsText) {
//            DCurrentPageButtonRenderer.Text(Text, FirstButton);
//            FirstButton = false;
//        }
//        for (int Index = 0; Index < DOptionsEditText.size(); Index++) {
//            if (!DOptionsEditValidationFunctions[Index] (DOptionsEditText[Index])) {
//                AllInputsValid = false;
//                break;
//            }
//        }
//
//        DCurrentPageButtonRenderer.Width(DCurrentPageButtonRenderer.Width() * 3 / 2);
//        DCurrentPageButtonRenderer.Height(DCurrentPageButtonRenderer.Height() * 3 / 2);
//        ButtonLeft = BufferWidth - DBorderWidth - DCurrentPageButtonRenderer.Width();
//        ButtonSkip = DCurrentPageButtonRenderer.Height() * 3 / 2;
//        ButtonTop = BufferHeight - DBorderWidth - (DCurrentPageButtonsText.size() * ButtonSkip - DCurrentPageButtonRenderer.Height() / 2);
//
//        if ((ButtonLeft <= DCurrentX[pcNone.ordinal()]) && ((ButtonLeft + DCurrentPageButtonRenderer
//                .Width() > DCurrentX[pcNone.ordinal()]))) {
//            ButtonXAlign = true;
//        }
//        FirstButton = true;
//        for (String Text : DCurrentPageButtonsText) {
//            CButtonRenderer::EButtonState ButtonState = CButtonRenderer::bsNone;
//
//            DCurrentPageButtonRenderer.Text(Text);
//            if (ButtonXAlign) {
//                if ((ButtonTop <= DCurrentY[pcNone.ordinal()]) && ((ButtonTop +
//                        DCurrentPageButtonRenderer.Height() > DCurrentY[pcNone.ordinal()]))) {
//                    ButtonState = DLeftDown[pcNone.ordinal()] ? CButtonRenderer::bsPressed :
//                            CButtonRenderer::bsHover;
//                    ButtonHovered = true;
//                }
//            }
//            if (FirstButton && !AllInputsValid) {
//                if (CButtonRenderer::bsNone != ButtonState) {
//                    ButtonHovered = false;
//                }
//                ButtonState = CButtonRenderer::bsInactive;
//            }
//            FirstButton = false;
//            DCurrentPageButtonRenderer.DrawButton(DWorkingBufferPixmap, ButtonLeft, ButtonTop, ButtonState);
//            if (CButtonRenderer::bsInactive != ButtonState) {
//                DCurrentPageButtonLocations.add(SRectangle({ButtonLeft, ButtonTop, DCurrentPageButtonRenderer.Width(), DCurrentPageButtonRenderer.Height()}));
//            } else {
//                DCurrentPageButtonLocations.add(SRectangle({0, 0, 0, 0}));
//            }
//            ButtonTop += ButtonSkip;
//        }
//
//        DOptionsEditLocations.clear();
//        BufferCenter = BufferWidth / 2;
//        OptionSkip = DOptionsEditRenderer.Height() * 3 / 2;
//        OptionTop = (BufferHeight + TitleHeight) / 2 - (OptionSkip * DOptionsEditTitles.size()) / 2;
//        for (int Index = 0; Index < DOptionsEditTitles.size(); Index++) {
//            String TempString;
//            int TextWidth, TextHeight;
//            TempString = DOptionsEditTitles[Index];
//
//            DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//            TextOffsetY = DOptionsEditRenderer.Height() / 2 - TextHeight / 2;
//            DFonts[CUnitDescriptionRenderer::fsLarge].DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, BufferCenter - TextWidth, OptionTop + TextOffsetY, WhiteColor, ShadowColor, 1, TempString);
//
//            DOptionsEditRenderer.Text(DOptionsEditText[Index], DOptionsEditValidationFunctions[Index]
//            (DOptionsEditText[Index]));
//            DOptionsEditRenderer.DrawEdit(DWorkingBufferPixmap, BufferCenter, OptionTop, Index == DOptionsEditSelected ? DOptionsEditSelectedCharacter : -1);
//            DOptionsEditLocations.add(SRectangle({BufferCenter, OptionTop, DOptionsEditRenderer.Width(), DOptionsEditRenderer.Height()}));
//            OptionTop += OptionSkip;
//        }
//
//        if (!DCurrentPageButtonHovered && ButtonHovered) {
//            DSoundLibraryMixer.PlayClip(DSoundLibraryMixer.FindClip("tick"), DSoundVolume, 0.0);
//        }
//        if (DNextGameMode != DGameMode) {
//            DSoundLibraryMixer.PlayClip(DSoundLibraryMixer.FindClip("place"), DSoundVolume, 0.0);
//        }
//        DCurrentPageButtonHovered = ButtonHovered;
    }


    /**
     * Sets the selected map to the passed in mapname.
     * @param mapName Map to be selected
     */
    // TODO: How to have all these objects share the same reference?
    // Currently having to reset them
    public void ProcessInputMapSelectMode(String mapName) {
        if (mapName == null) {
            Log.e("CApplicationData", "Map name is null.");
            return;
        }

        DSelectedMapIndex = CAssetDecoratedMap.FindMapIndex(mapName);
        DSelectedMap = CAssetDecoratedMap.GetMap(DSelectedMapIndex);
        DMapRenderer.DMap(DSelectedMap);
        // Map functions the same without these resetting
        //DMiniMapRenderer.DMapRenderer(DMapRenderer);
        //DMiniMapRenderer.DAssetRenderer(DAssetRenderer);
    }

    private void CalculateMapSelectMode() {

    }

    /**
     * Handles rendering the map select mode.
     * TODO: How much of this can be ported into the MapSelectActivity?
     * TODO: Some logic is kept here; perhaps just View updating in the activity?
     */
    private void RenderMapSelectMode() {
//        int BufferWidth, BufferHeight;
//        int MiniMapWidth, MiniMapHeight, MiniMapCenter, MiniMapLeft;
//        int ListViewWidth = 0, ListViewHeight = 0;
//        int TitleHeight;
//        int TextWidth, TextHeight, TextTop;
//        int TextColor, ShadowColor;
//        String PageTitle = "Select Map", TempString;
//        CButtonRenderer::EButtonState ButtonState = CButtonRenderer::bsNone;
//        boolean ButtonXAlign = false, ButtonHovered = false;
//
//        RenderMenuTitle(PageTitle, TitleHeight, BufferWidth, BufferHeight);
//
//        gdk_pixmap_get_size(DMiniMapPixmap, MiniMapWidth, MiniMapHeight);
//        if (null != DMapSelectListViewPixmap) {
//            gdk_pixmap_get_size(DMapSelectListViewPixmap, ListViewWidth, ListViewHeight);
//        }
//
//        if ((ListViewHeight != (BufferHeight - TitleHeight - DInnerBevel.Width() -
//                DBorderWidth)) || (ListViewWidth != (BufferWidth - DViewportXOffset -
//                DBorderWidth - DInnerBevel.Width() * 2))) {
//            if (null != DMapSelectListViewPixmap) {
//                g_object_unref(DMapSelectListViewPixmap);
//            }
//            ListViewHeight = BufferHeight - TitleHeight - DInnerBevel.Width() - DBorderWidth;
//            ListViewWidth = BufferWidth - DViewportXOffset - DBorderWidth - DInnerBevel.Width() * 2;
//            DMapSelectListViewPixmap = gdk_pixmap_new(DDrawingArea.window, ListViewWidth, ListViewHeight, -1);
//        }
//
//        DMapSelectListViewXOffset = DBorderWidth;
//        DMapSelectListViewYOffset = TitleHeight + DInnerBevel.Width();
//
//        gdk_draw_pixmap(DMapSelectListViewPixmap, DDrawingContext, DWorkingBufferPixmap,
//                DMapSelectListViewXOffset, DMapSelectListViewYOffset, 0, 0, ListViewWidth,
//                ListViewHeight);
//
//        ArrayList<String> MapNames;
//        while (CAssetDecoratedMap::GetMap (MapNames.size())) {
//            MapNames.add(CAssetDecoratedMap::GetMap (MapNames.size()).MapName());
//        }
//
//        DMapSelectListViewRenderer.DrawListView(DMapSelectListViewPixmap, DSelectedMapIndex,
//                DSelectedMapOffset, MapNames);
//        gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DMapSelectListViewPixmap, 0, 0, DMapSelectListViewXOffset,
//                DMapSelectListViewYOffset, ListViewWidth, ListViewHeight);
//        DInnerBevel.DrawBevel(DWorkingBufferPixmap, DMapSelectListViewXOffset, DMapSelectListViewYOffset, ListViewWidth, ListViewHeight);
//
//        DMiniMapRenderer.DrawMiniMap(DMiniMapPixmap);
//        MiniMapLeft = DMapSelectListViewXOffset + ListViewWidth + DInnerBevel.Width() * 4;
//        gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DMiniMapPixmap, 0, 0, MiniMapLeft, DMapSelectListViewYOffset, -1, -1);
//        DInnerBevel.DrawBevel(DWorkingBufferPixmap, MiniMapLeft, DMapSelectListViewYOffset, MiniMapWidth, MiniMapHeight);
//
//        TextTop = DMapSelectListViewYOffset + MiniMapHeight + DInnerBevel.Width() * 2;
//        MiniMapCenter = MiniMapLeft + MiniMapWidth / 2;
//        TextColor = DFonts[CUnitDescriptionRenderer::fsLarge].FindPixel("white");
//        ShadowColor = DFonts[CUnitDescriptionRenderer::fsLarge].FindPixel("black");
//
//        TempString = std::to_string (DSelectedMap.PlayerCount()) + " Players";
//        DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//        DFonts[CUnitDescriptionRenderer::fsLarge].DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, MiniMapCenter -
//                TextWidth / 2, TextTop, TextColor, ShadowColor, 1, TempString);
//        TextTop += TextHeight;
//        TempString = std::to_string (DSelectedMap.Width()) + " x " + std::to_string
//        (DSelectedMap.Height());
//        DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//        DFonts[CUnitDescriptionRenderer::fsLarge].DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, MiniMapCenter -
//                TextWidth / 2, TextTop, TextColor, ShadowColor, 1, TempString);
//        TextTop += TextHeight * 3 / 2;
//        DCurrentPageButtonLocations.resize(2);
//
//        if ((MiniMapLeft <= DCurrentX[pcNone.ordinal()]) && (MiniMapLeft + MiniMapWidth >
//                DCurrentX[pcNone.ordinal()])) {
//            ButtonXAlign = true;
//        }
//
//        DCurrentPageButtonsFunctions.clear();
//
//        TempString = "Select";
//        DCurrentPageButtonRenderer.Text(TempString, true);
//        DCurrentPageButtonRenderer.Height(DCurrentPageButtonRenderer.Height() * 3 / 2);
//        DCurrentPageButtonRenderer.Width(MiniMapWidth);
//
//        TextTop = BufferHeight - DBorderWidth - (DCurrentPageButtonRenderer.Height() * 9 / 4);
//        if (ButtonXAlign) {
//            if ((TextTop <= DCurrentY[pcNone.ordinal()]) && ((TextTop + DCurrentPageButtonRenderer
//                    .Height() > DCurrentY[pcNone.ordinal()]))) {
//                ButtonState = DLeftDown[pcNone.ordinal()] ? CButtonRenderer::bsPressed :
//                        CButtonRenderer::bsHover;
//                ButtonHovered = true;
//            }
//        }
//        DCurrentPageButtonRenderer.DrawButton(DWorkingBufferPixmap, MiniMapLeft, TextTop, ButtonState);
//        DCurrentPageButtonLocations[0] = SRectangle({MiniMapLeft, TextTop, DCurrentPageButtonRenderer.Width(), DCurrentPageButtonRenderer.Height()});
//        DCurrentPageButtonsFunctions.add(SelectMapButtonCallback);
//
//        TextTop = BufferHeight - DBorderWidth - DCurrentPageButtonRenderer.Height();
//        ButtonState = CButtonRenderer::bsNone;
//        if (ButtonXAlign) {
//            if ((TextTop <= DCurrentY[pcNone.ordinal()]) && ((TextTop + DCurrentPageButtonRenderer
//                    .Height() > DCurrentY[pcNone.ordinal()]))) {
//                ButtonState = DLeftDown[pcNone.ordinal()] ? CButtonRenderer::bsPressed :
//                        CButtonRenderer::bsHover;
//                ButtonHovered = true;
//            }
//        }
//        TempString = "Cancel";
//        DCurrentPageButtonRenderer.Text(TempString);
//        DCurrentPageButtonRenderer.DrawButton(DWorkingBufferPixmap, MiniMapLeft, TextTop, ButtonState);
//        DCurrentPageButtonLocations[1] = new SRectangle({MiniMapLeft, TextTop, DCurrentPageButtonRenderer.Width(),
//                DCurrentPageButtonRenderer.Height()});
//        DCurrentPageButtonsFunctions.add(MainMenuButtonCallback);
//
//        if (!DCurrentPageButtonHovered && ButtonHovered) {
//            DSoundLibraryMixer.PlayClip(DSoundLibraryMixer.FindClip("tick"), DSoundVolume, 0.0);
//        }
//        if (DNextGameMode != DGameMode) {
//            DSoundLibraryMixer.PlayClip(DSoundLibraryMixer.FindClip("place"), DSoundVolume, 0.0);
//        }
//        DCurrentPageButtonHovered = ButtonHovered;
    }

    private void ProcessInputHostSelectMode() {
//        if (DLeftClick[pcNone.ordinal()] && !DLeftDown[pcNone.ordinal()]) {
//            int ItemSelected = DLobbySelectListViewRenderer->ItemAt(DCurrentX[pcNone] - DLobbySelectListViewXOffset, DCurrentY[pcNone] - DLobbySelectListViewYOffset);
//            if (CListViewRenderer::lvoUpArrow == ItemSelected) {
//                if (DSelectedGameOffset) {
//                    DSelectedGameOffset--;
//                }
//            }
//            else if (CListViewRenderer::lvoDownArrow == ItemSelected) {
//                DSelectedGameOffset++;
//            }
//            else if (CListViewRenderer::lvoNone != ItemSelected) {
//                if (DSelectedGameIndex != ItemSelected) {
//                    DSelectedGameIndex = ItemSelected;
//                    DSelectedGameName = DLobbyNames[DSelectedGameIndex];
//                }
//            }
//            else {
//                for (unsigned int Index = 0; Index < DCurrentPageButtonLocations.size(); Index++) {
//                    if ((DCurrentPageButtonLocations[Index].DXPosition <= DCurrentX[pcNone]) && ((DCurrentPageButtonLocations[Index].DXPosition + DCurrentPageButtonLocations[Index].DWidth > DCurrentX[pcNone]))) {
//                        if ((DCurrentPageButtonLocations[Index].DYPosition <= DCurrentY[pcNone]) && ((DCurrentPageButtonLocations[Index].DYPosition + DCurrentPageButtonLocations[Index].DHeight > DCurrentY[pcNone]))) {
//                            DCurrentPageButtonsFunctions[Index](this);
//                        }
//                    }
//                }
//            }
//        }
    }

    private void CalculateHostSelectMode() {

    }

    void RenderHostSelectMode() {
//        gint BufferWidth, BufferHeight;
//        gint MiniMapWidth, MiniMapHeight, /*MiniMapCenter,*/ MiniMapLeft;
//        gint ListViewWidth = 0, ListViewHeight = 0;
//        gint TitleHeight;
//
//        gint /*TextWidth, TextHeight,*/ TextTop;
//        //int TextColor, ShadowColor;
//        std::string PageTitle = "Select Game Lobby", TempString;
//        CButtonRenderer::EButtonState ButtonState = CButtonRenderer::bsNone;
//        bool ButtonXAlign = false, ButtonHovered = false;
//
//
//        RenderMenuTitle(PageTitle, TitleHeight, BufferWidth, BufferHeight);
//
//        gdk_pixmap_get_size(DMiniMapPixmap, &MiniMapWidth, &MiniMapHeight);
//        if (nullptr != DLobbySelectListViewPixmap) {
//            gdk_pixmap_get_size(DLobbySelectListViewPixmap, &ListViewWidth, &ListViewHeight);
//        }
//
//        if ((ListViewHeight != (BufferHeight - TitleHeight - DInnerBevel->Width() - DBorderWidth)) || (ListViewWidth != (BufferWidth - DViewportXOffset - DBorderWidth - DInnerBevel->Width() * 2))) {
//            if (nullptr != DLobbySelectListViewPixmap) {
//                g_object_unref(DLobbySelectListViewPixmap);
//            }
//            ListViewHeight = BufferHeight - TitleHeight - DInnerBevel->Width() - DBorderWidth;
//            ListViewWidth = BufferWidth - DViewportXOffset - DBorderWidth - DInnerBevel->Width() * 2;
//            DLobbySelectListViewPixmap = gdk_pixmap_new(DDrawingArea->window, ListViewWidth, ListViewHeight, -1);
//        }
//
//        DLobbySelectListViewXOffset = DBorderWidth;
//        DLobbySelectListViewYOffset = TitleHeight + DInnerBevel->Width();
//
//        gdk_draw_pixmap(DLobbySelectListViewPixmap, DDrawingContext, DWorkingBufferPixmap, DLobbySelectListViewXOffset, DLobbySelectListViewYOffset, 0, 0, ListViewWidth, ListViewHeight);
//
//        DLobbySelectListViewRenderer->DrawListView(DLobbySelectListViewPixmap, DSelectedGameIndex, DSelectedGameOffset, DLobbyNames);
//        gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DLobbySelectListViewPixmap, 0, 0, DLobbySelectListViewXOffset, DLobbySelectListViewYOffset, ListViewWidth, ListViewHeight);
//        DInnerBevel->DrawBevel(DWorkingBufferPixmap, DLobbySelectListViewXOffset, DLobbySelectListViewYOffset, ListViewWidth, ListViewHeight);
//
//        //DMiniMapRenderer->DrawMiniMap(DMiniMapPixmap);
//        MiniMapLeft = DLobbySelectListViewXOffset + ListViewWidth + DInnerBevel->Width() * 4;
//        //gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DMiniMapPixmap, 0, 0, MiniMapLeft, DMapSelectListViewYOffset, -1, -1);
//        //DInnerBevel->DrawBevel(DWorkingBufferPixmap, MiniMapLeft, DMapSelectListViewYOffset, MiniMapWidth, MiniMapHeight);
//
//        TextTop = DLobbySelectListViewYOffset + MiniMapHeight + DInnerBevel->Width() * 2;
//        //MiniMapCenter = MiniMapLeft + MiniMapWidth / 2;
//        //TextColor = DFonts[CUnitDescriptionRenderer::fsLarge]->FindPixel("white");
//        //ShadowColor = DFonts[CUnitDescriptionRenderer::fsLarge]->FindPixel("black");
//    /*
//    //TODO: Here display lobby details from game
//    TempString = std::to_string(DSelectedMap->PlayerCount()) + " Players";
//    DFonts[CUnitDescriptionRenderer::fsLarge]->MeasureText(TempString, TextWidth, TextHeight);
//    DFonts[CUnitDescriptionRenderer::fsLarge]->DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, MiniMapCenter - TextWidth/2, TextTop, TextColor, ShadowColor, 1, TempString);
//    TextTop += TextHeight;
//    TempString = std::to_string(DSelectedMap->Width()) + " x " + std::to_string(DSelectedMap->Height());
//    DFonts[CUnitDescriptionRenderer::fsLarge]->MeasureText(TempString, TextWidth, TextHeight);
//    DFonts[CUnitDescriptionRenderer::fsLarge]->DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, MiniMapCenter - TextWidth/2, TextTop, TextColor, ShadowColor, 1, TempString);
//    TextTop += TextHeight * 3 / 2;
//    DCurrentPageButtonLocations.resize(2);*/
//
//        if ((MiniMapLeft <= DCurrentX[pcNone]) && (MiniMapLeft + MiniMapWidth > DCurrentX[pcNone])) {
//            ButtonXAlign = true;
//        }
//
//        DCurrentPageButtonsFunctions.clear();
//
//        TempString = "Select";
//        DCurrentPageButtonRenderer->Text(TempString, true);
//        DCurrentPageButtonRenderer->Height(DCurrentPageButtonRenderer->Height() * 3 / 2);
//        DCurrentPageButtonRenderer->Width(MiniMapWidth);
//
//        TextTop = BufferHeight - DBorderWidth - (DCurrentPageButtonRenderer->Height() * 9 / 4);
//        if ("" == DSelectedGameName) {
//            ButtonState = CButtonRenderer::bsInactive;
//        }
//        else if (ButtonXAlign) {
//            if ((TextTop <= DCurrentY[pcNone]) && ((TextTop + DCurrentPageButtonRenderer->Height() > DCurrentY[pcNone]))) {
//                ButtonState = DLeftDown[pcNone] ? CButtonRenderer::bsPressed : CButtonRenderer::bsHover;
//                ButtonHovered = true;
//            }
//        }
//        DCurrentPageButtonRenderer->DrawButton(DWorkingBufferPixmap, MiniMapLeft, TextTop, ButtonState);
//        DCurrentPageButtonLocations[0] = SRectangle({MiniMapLeft, TextTop, DCurrentPageButtonRenderer->Width(), DCurrentPageButtonRenderer->Height()});
//        DCurrentPageButtonsFunctions.push_back(JoinGameLobbyCallback);
//
//        TextTop = BufferHeight - DBorderWidth - DCurrentPageButtonRenderer->Height();
//        ButtonState = CButtonRenderer::bsNone;
//        if (ButtonXAlign) {
//            if ((TextTop <= DCurrentY[pcNone]) && ((TextTop + DCurrentPageButtonRenderer->Height() > DCurrentY[pcNone]))) {
//                ButtonState = DLeftDown[pcNone] ? CButtonRenderer::bsPressed : CButtonRenderer::bsHover;
//                ButtonHovered = true;
//            }
//        }
//        TempString = "Cancel";
//        DCurrentPageButtonRenderer->Text(TempString);
//        DCurrentPageButtonRenderer->DrawButton(DWorkingBufferPixmap, MiniMapLeft, TextTop, ButtonState);
//        DCurrentPageButtonLocations[1] = SRectangle({MiniMapLeft, TextTop, DCurrentPageButtonRenderer->Width(), DCurrentPageButtonRenderer->Height()});
//        DCurrentPageButtonsFunctions.push_back(OnlineMultiPlayerButtonCallback);
//
//        if (!DCurrentPageButtonHovered && ButtonHovered) {
//            DSoundLibraryMixer->PlayClip(DSoundLibraryMixer->FindClip("tick"), DSoundVolume, 0.0);
//        }
//        if (DNextGameMode != DGameMode) {
//            DSoundLibraryMixer->PlayClip(DSoundLibraryMixer->FindClip("place"), DSoundVolume, 0.0);
//        }
//        DCurrentPageButtonHovered = ButtonHovered;
    }

    private void ProcessInputLobbyMode() {

    }

    private void CalculateLobbyMode() {

       CMultiplayer.PollForGameUpdates poll = DMultiplayer.new PollForGameUpdates();
        poll.execute();
//        while (poll.getStatus() != AsyncTask.Status.FINISHED) {
//            System.out.println("Polling AsyncTask running");
//        }

//        if (poll.requestStatus) {
//            Log.d("CalculateLobbyMode", "Received updated Game" + DMultiplayer.getGame
//                    ().getHost() + DMultiplayer.getGame().getPlayers().size());
//            for (int index = 0; index < DMultiplayer.getGame().getNumPlayers(); index++) {
//                CMultiplayer.PlayerInfo player = DMultiplayer.getGame().getPlayers().get(index);
//                //If the player is the user
//                if (((0 == index) && (gstMultiPlayerHost == DGameSessionType)) || ((Objects
//                        .equals(player.name, DUsername)) && (gstMultiPlayerClient ==
//                        DGameSessionType))) {
//                    DPlayerColor = EPlayerColor.values()[player.color];
//                    DPlayerStatus = player.ready == 1;
//                }
//                DLoadingPlayerColors[index + 1] = EPlayerColor.values()[player.color];
//                DLoadingPlayerStatus[index + 1] = player.ready == 1;
//                DLoadingPlayerTypes[index + 1] = EPlayerType.values()[player.type];
//            }
//
//            if (DMultiplayer.getGame().getStartGame()) {
//                Log.i("CalculateLobbyMode", "Multiplayer Game Start");
//                DInGameFlag = true;
//                LoadGameMap(DSelectedMapIndex);
//                ChangeMode(gmBattle);
//                DSoundLibraryMixer.PlaySong(DSoundLibraryMixer.FindSong("game1"), DMusicVolume);
//            }
//        }
    }

    private void RenderLobbyMode() {

    }

    private void ProcessInputPlayerAISelectMode() {
//        DPlayerColorRequestingChange = pcNone;
//        DPlayerColorChangeRequest = pcNone;
//        DPlayerColorRequestTypeChange = pcNone;
//        if (DLeftClick[pcNone.ordinal()] != 0 && !DLeftDown[pcNone.ordinal()]) {
//            for (int Index = 0; Index < DColorButtonLocations.size(); Index++) {
//                if ((DColorButtonLocations[Index].DXPosition <= DCurrentX[pcNone.ordinal()]) && (
//                        (DColorButtonLocations[Index].DXPosition + DColorButtonLocations[Index]
//                                .DWidth > DCurrentX[pcNone.ordinal()]))) {
//                    if ((DColorButtonLocations[Index].DYPosition <= DCurrentY[pcNone.ordinal()]) && ((DColorButtonLocations[Index]
//                            .DYPosition + DColorButtonLocations[Index].DHeight > DCurrentY[pcNone.ordinal()]))) {
//                        int PlayerSelecting = 1 + (Index / (pcMax.ordinal() - 1));
//                        int ColorSelecting = 1 + (Index % (pcMax.ordinal() - 1));
//
//                        if ((PlayerSelecting == DPlayerColor) || (gstMultiPlayerClient != DGameSessionType)) {
//                            if ((PlayerSelecting == DPlayerColor) || (ptHuman != DLoadingPlayerTypes[PlayerSelecting])) {
//                                DPlayerColorRequestingChange = (EPlayerColor) PlayerSelecting;
//                                DPlayerColorChangeRequest = (EPlayerColor) ColorSelecting;
//                            }
//                        }
//                    }
//                }
//            }
//            for (int Index = 0; Index < DCurrentPageButtonLocations.size(); Index++) {
//                if ((DCurrentPageButtonLocations[Index].DXPosition <= DCurrentX[pcNone.ordinal()]) &&
//                        ((DCurrentPageButtonLocations[Index].DXPosition +
//                                DCurrentPageButtonLocations[Index].DWidth > DCurrentX[pcNone.ordinal
//                                ()]))) {
//                    if ((DCurrentPageButtonLocations[Index].DYPosition <= DCurrentY[pcNone.ordinal()
//                            ]) && ((DCurrentPageButtonLocations[Index].DYPosition +
//                            DCurrentPageButtonLocations[Index].DHeight > DCurrentY[pcNone.ordinal()]))) {
//                        DCurrentPageButtonsFunctions[Index] (this);
//                    }
//                }
//            }
//            for (int Index = 0; Index < DPlayerTypeButtonLocations.size(); Index++) {
//                if ((DPlayerTypeButtonLocations[Index].DXPosition <= DCurrentX[pcNone.ordinal
//                        ()]) && ((DPlayerTypeButtonLocations[Index].DXPosition +
//                        DPlayerTypeButtonLocations[Index].DWidth > DCurrentX[pcNone.ordinal()
//                        ]))) {
//                    if ((DPlayerTypeButtonLocations[Index].DYPosition <= DCurrentY[pcNone.ordinal()]) && (
//                            (DPlayerTypeButtonLocations[Index]
//                                    .DYPosition + DPlayerTypeButtonLocations[Index].DHeight >
//                                    DCurrentY[pcNone.ordinal()]))) {
//                        DPlayerColorRequesTypeChange = (CGameDataTypes.EPlayerColor) (Index + 2);
//                        break;
//                    }
//                }
//            }
//        }
    }

    void CalculatePlayerAISelectMode() {
        DLoadingPlayerTypes[0] = EPlayerType.ptHuman;
        DLoadingPlayerTypes[1] = EPlayerType.ptAIEasy;
//        if (pcNone != DPlayerColorRequestingChange) {

//            CGameDataTypes.EPlayerColor NewColorInUse = pcNone;
//            for (int Index = 1; Index < pcMax.ordinal(); Index++) {
//                if (Index != DPlayerColorRequestingChange.ordinal()) {
//                    if (ptNone != DLoadingPlayerTypes[Index]) {
//                        if (DLoadingPlayerColors[Index] == DPlayerColorChangeRequest) {
//                            NewColorInUse = (CGameDataTypes.EPlayerColor) Index;
//                            break;
//                        }
//                    }
//                }
//            }
//            if (pcNone != NewColorInUse) {
//                DLoadingPlayerColors[NewColorInUse.ordinal()] =
//                        DLoadingPlayerColors[DPlayerColorRequestingChange.ordinal()];
//            }
//
//            DLoadingPlayerColors[DPlayerColorRequestingChange.ordinal()] =
//                    DPlayerColorChangeRequest;
//
//            *DSelectedMap =*CAssetDecoratedMap::DuplicateMap
//            (DSelectedMapIndex, DLoadingPlayerColors);
//        }
//        if (pcNone != DPlayerColorRequesTypeChange) {
//            if (gstSinglePlayer == DGameSessionType) {
//                switch (DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()]) {
//                    case ptAIEasy:
//                        DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()] = ptAIMedium;
//                        break;
//                    case ptAIMedium:
//                        DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()] = ptAIHard;
//                        break;
//                    default:
//                        DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()] = ptAIEasy;
//                        break;
//                }
//            } else if (gstMultiPlayerHost == DGameSessionType) {
//                switch (DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()]) {
//                    case ptHuman:
//                        DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()] = ptAIEasy;
//                        break;
//                    case ptAIEasy:
//                        DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()] = ptAIMedium;
//                        break;
//                    case ptAIMedium:
//                        DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()] = ptAIHard;
//                        break;
//                    case ptAIHard:
//                        DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()] = ptNone;
//                        break;
//                    default:
//                        DLoadingPlayerTypes[DPlayerColorRequesTypeChange.ordinal()] = ptHuman;
//                        break;
//                }
//            }
//        }
    }

    void RenderPlayerAISelectMode() {
//        int BufferWidth, BufferHeight;
//        int TitleHeight;
//        int TextWidth, TextHeight, MaxTextWidth;
//        int ColumnWidth, RowHeight;
//        int MiniMapWidth, MiniMapHeight, MiniMapCenter, MiniMapLeft;
//        int TextTop, ButtonLeft, ButtonTop, AIButtonLeft, ColorButtonHeight;
//        int GoldColor, WhiteColor, ShadowColor;
//        String TempString;
//        CButtonRenderer::EButtonState ButtonState = CButtonRenderer::bsNone;
//        boolean ButtonXAlign = false, ButtonHovered = false;
//
//        RenderMenuTitle("Select Colors/Difficulty", TitleHeight, BufferWidth, BufferHeight);
//
//        GoldColor = DFonts[CUnitDescriptionRenderer::fsLarge].FindPixel("gold");
//        WhiteColor = DFonts[CUnitDescriptionRenderer::fsLarge].FindPixel("white");
//        ShadowColor = DFonts[CUnitDescriptionRenderer::fsLarge].FindPixel("black");
//
//        gdk_pixmap_get_size(DMiniMapPixmap, MiniMapWidth, MiniMapHeight);
//
//        DMiniMapRenderer.DrawMiniMap(DMiniMapPixmap);
//        MiniMapLeft = BufferWidth - MiniMapWidth - DBorderWidth;
//        gdk_draw_pixmap(DWorkingBufferPixmap, DDrawingContext, DMiniMapPixmap, 0, 0, MiniMapLeft, TitleHeight + DInnerBevel.Width(), -1,
//                -1);
//        DInnerBevel.DrawBevel(DWorkingBufferPixmap, MiniMapLeft, TitleHeight + DInnerBevel.Width(), MiniMapWidth, MiniMapHeight);
//
//        TextTop = TitleHeight + MiniMapHeight + DInnerBevel.Width() * 3;
//        MiniMapCenter = MiniMapLeft + MiniMapWidth / 2;
//
//        TempString = std::to_string (DSelectedMap.PlayerCount()) + " Players";
//        DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//        DFonts[CUnitDescriptionRenderer::fsLarge].DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, MiniMapCenter - TextWidth / 2,
//                TextTop, WhiteColor, ShadowColor, 1, TempString);
//        TextTop += TextHeight;
//        TempString = std::to_string (DSelectedMap.Width()) + " x " + std::to_string
//        (DSelectedMap.Height());
//        DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//        DFonts[CUnitDescriptionRenderer::fsLarge].DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, MiniMapCenter - TextWidth / 2,
//                TextTop, WhiteColor, ShadowColor, 1, TempString);
//
//
//        TextTop = TitleHeight;
//        TempString = "Player";
//        DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//        DFonts[CUnitDescriptionRenderer::fsLarge].DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, DBorderWidth, TextTop,
//                WhiteColor, ShadowColor, 1, TempString);
//        TextTop += TextHeight;
//
//        DCurrentPageButtonRenderer.Text("AI Easy", true);
//        ColorButtonHeight = DCurrentPageButtonRenderer.Height();
//        RowHeight = DCurrentPageButtonRenderer.Height() + DInnerBevel.Width() * 2;
//        if (RowHeight < TextHeight) {
//            RowHeight = TextHeight;
//        }
//        DCurrentPageButtonRenderer.Text("X", true);
//        DCurrentPageButtonRenderer.Height(ColorButtonHeight);
//        ColumnWidth = DCurrentPageButtonRenderer.Width() + DInnerBevel.Width() * 2;
//        MaxTextWidth = 0;
//        for (int Index = 1; Index <= DSelectedMap.PlayerCount(); Index++) {
//            if (Index == DPlayerColor.ordinal()) {
//                DPlayerNames[Index] = TempString = std::to_string (Index) + ". You";
//            } else if (ptHuman != DLoadingPlayerTypes[Index]) {
//                DPlayerNames[Index] = TempString = std::to_string
//                (Index) + ". Player " + std::to_string (Index);
//            }
//            DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//            if (MaxTextWidth < TextWidth) {
//                MaxTextWidth = TextWidth;
//            }
//        }
//        TempString = "Color";
//        DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//        DFonts[CUnitDescriptionRenderer::fsLarge].DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, DBorderWidth + MaxTextWidth + (ColumnWidth * (pcMax + 1)) / 2 - TextWidth / 2, TitleHeight, WhiteColor, ShadowColor, 1, TempString);
//        DColorButtonLocations.clear();
//        for (int Index = 1; Index <= DSelectedMap.PlayerCount(); Index++) {
//            TempString = DPlayerNames[Index];
//            DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//            DFonts[CUnitDescriptionRenderer::fsLarge].DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, DBorderWidth, TextTop, Index == DPlayerColor ? GoldColor : WhiteColor, ShadowColor, 1, TempString);
//            for (int ColorIndex = 1; ColorIndex < pcMax; ColorIndex++) {
//                int ButtonLeft = DBorderWidth + MaxTextWidth + ColorIndex * ColumnWidth;
//                DCurrentPageButtonRenderer.Text(DLoadingPlayerColors[Index] == ColorIndex ? "X" : "");
//                DCurrentPageButtonRenderer.ButtonColor((EPlayerColor) ColorIndex);
//                DCurrentPageButtonRenderer.DrawButton(DWorkingBufferPixmap, ButtonLeft, TextTop, CButtonRenderer::bsNone);
//                DColorButtonLocations.add(SRectangle({ButtonLeft, TextTop, DCurrentPageButtonRenderer.Width(), DCurrentPageButtonRenderer.Height()}));
//                AIButtonLeft = ButtonLeft + ColumnWidth;
//            }
//            TextTop += RowHeight;
//        }
//
//
//        DCurrentPageButtonRenderer.ButtonColor(pcNone.ordinal());
//        TempString = "AI Easy";
//        DCurrentPageButtonRenderer.Text(TempString);
//        DCurrentPageButtonRenderer.Width(DCurrentPageButtonRenderer.Width() * 3 / 2);
//
//        TextTop = TitleHeight;
//        TempString = "Difficulty";
//        DFonts[CUnitDescriptionRenderer::fsLarge].MeasureText(TempString, TextWidth, TextHeight);
//        DFonts[CUnitDescriptionRenderer::fsLarge].DrawTextWithShadow(DWorkingBufferPixmap, DDrawingContext, AIButtonLeft + (DCurrentPageButtonRenderer.Width() - TextWidth) / 2, TextTop, WhiteColor, ShadowColor, 1, TempString);
//
//        ButtonXAlign = false;
//        if ((AIButtonLeft <= DCurrentX[pcNone]) && (AIButtonLeft + DCurrentPageButtonRenderer.Width() > DCurrentX[pcNone])) {
//            ButtonXAlign = true;
//        }
//        TextTop += RowHeight + TextHeight;
//        DPlayerTypeButtonLocations.clear();
//        for (int Index = 2; Index <= DSelectedMap.PlayerCount(); Index++) {
//            switch (DLoadingPlayerTypes[Index]) {
//                case ptHuman:
//                    DCurrentPageButtonRenderer.Text("Human");
//                    break;
//                case ptAIEasy:
//                    DCurrentPageButtonRenderer.Text("AI Easy");
//                    break;
//                case ptAIMedium:
//                    DCurrentPageButtonRenderer.Text("AI Medium");
//                    break;
//                case ptAIHard:
//                    DCurrentPageButtonRenderer.Text("AI Hard");
//                    break;
//                default:
//                    DCurrentPageButtonRenderer.Text("Closed");
//                    break;
//            }
//            ButtonState = CButtonRenderer::bsNone;
//            if (ButtonXAlign) {
//                if ((TextTop <= DCurrentY[pcNone.ordinal()]) && ((TextTop + DCurrentPageButtonRenderer.Height() >
//                        DCurrentY[pcNone.ordinal()]))) {
//                    ButtonState = DLeftDown[pcNone] ? CButtonRenderer::bsPressed : CButtonRenderer::bsHover;
//                    ButtonHovered = true;
//                }
//            }
//            DCurrentPageButtonRenderer.DrawButton(DWorkingBufferPixmap, AIButtonLeft, TextTop, ButtonState);
//            DPlayerTypeButtonLocations.add(SRectangle({AIButtonLeft, TextTop, DCurrentPageButtonRenderer.Width(), DCurrentPageButtonRenderer.Height()}));
//
//            TextTop += RowHeight;
//        }
//
//        DCurrentPageButtonLocations.clear();
//        DCurrentPageButtonsFunctions.clear();
//
//        DCurrentPageButtonRenderer.ButtonColor(pcNone);
//        TempString = "Play Game";
//        DCurrentPageButtonRenderer.Text(TempString, true);
//        DCurrentPageButtonRenderer.Height(DCurrentPageButtonRenderer.Height() * 3 / 2);
//        DCurrentPageButtonRenderer.Width(MiniMapWidth);
//        ButtonLeft = BufferWidth - DCurrentPageButtonRenderer.Width() - DBorderWidth;
//        ButtonTop = BufferHeight - (DCurrentPageButtonRenderer.Height() * 9 / 4) - DBorderWidth;
//        ButtonState = CButtonRenderer::bsNone;
//        if ((ButtonLeft <= DCurrentX[pcNone.ordinal()]) && (ButtonLeft + DCurrentPageButtonRenderer.Width() >
//                DCurrentX[pcNone.ordinal()])) {
//            ButtonXAlign = true;
//        }
//        if (ButtonXAlign) {
//            if ((ButtonTop <= DCurrentY[pcNone.ordinal()]) && ((ButtonTop + DCurrentPageButtonRenderer.Height() >
//                    DCurrentY[pcNone.ordinal()]))) {
//                ButtonState = DLeftDown[pcNone] ? CButtonRenderer::bsPressed : CButtonRenderer::bsHover;
//                ButtonHovered = true;
//            }
//        }
//        DCurrentPageButtonRenderer.DrawButton(DWorkingBufferPixmap, ButtonLeft, ButtonTop, ButtonState);
//        DCurrentPageButtonLocations.add(SRectangle({ButtonLeft, ButtonTop, DCurrentPageButtonRenderer.Width(), DCurrentPageButtonRenderer.Height()}));
//        DCurrentPageButtonsFunctions.add(PlayGameButtonCallback);
//
//        ButtonTop = BufferHeight - DCurrentPageButtonRenderer.Height() - DBorderWidth;
//        ButtonState = CButtonRenderer::bsNone;
//        if (ButtonXAlign) {
//            if ((ButtonTop <= DCurrentY[pcNone]) && ((ButtonTop + DCurrentPageButtonRenderer.Height() > DCurrentY[pcNone]))) {
//                ButtonState = DLeftDown[pcNone] ? CButtonRenderer::bsPressed : CButtonRenderer::bsHover;
//                ButtonHovered = true;
//            }
//        }
//        TempString = "Cancel";
//        DCurrentPageButtonRenderer.Text(TempString, false);
//        DCurrentPageButtonRenderer.DrawButton(DWorkingBufferPixmap, ButtonLeft, ButtonTop, ButtonState);
//        DCurrentPageButtonLocations.add(SRectangle({ButtonLeft, ButtonTop, DCurrentPageButtonRenderer.Width(), DCurrentPageButtonRenderer.Height()}));
//        DCurrentPageButtonsFunctions.add(MainMenuButtonCallback);
//
//        if (!DCurrentPageButtonHovered && ButtonHovered) {
//            DSoundLibraryMixer.PlayClip(DSoundLibraryMixer.FindClip("tick"), DSoundVolume, 0.0);
//        }
//        if (DNextGameMode != DGameMode) {
//            DSoundLibraryMixer.PlayClip(DSoundLibraryMixer.FindClip("place"), DSoundVolume, 0.0);
//        }
//        DCurrentPageButtonHovered = ButtonHovered;
    }

    public EGameMode DGameMode() {
        return DGameMode;
    }

    private void ChangeMode(EGameMode nextMode) {
        DNextGameMode = nextMode;
    }

//    void SwitchButtonMenuData() {
//        boolean Changed = false;
//        if (gmMainMenu == DNextGameMode) {
//            DCurrentPageTitle = DMainMenuTitle;
//            DCurrentPageButtonsText = DMainMenuButtonsText;
//            DCurrentPageButtonsFunctions = DMainMenuButtonsFunctions;
//            Changed = true;
//        } else if (gmOptionsMenu == DNextGameMode) {
//            DCurrentPageTitle = DOptionsMenuTitle;
//            DCurrentPageButtonsText = DOptionsMenuButtonsText;
//            DCurrentPageButtonsFunctions = DOptionsMenuButtonsFunctions;
//            Changed = true;
//        } else if (gmMultiPlayerOptionsMenu == DNextGameMode) {
//            DCurrentPageTitle = DMultiPlayerOptionsMenuTitle;
//            DCurrentPageButtonsText = DMultiPlayerOptionsMenuButtonsText;
//            DCurrentPageButtonsFunctions = DMultiPlayerOptionsMenuButtonsFunctions;
//            Changed = true;
//        } else if (gmGameMenu == DNextGameMode) {
//            //JPF: Added button switch logic for going to Game Menu
//            DCurrentPageTitle = DGameMenuTitle;
//            DCurrentPageButtonsText = DGameMenuButtonsText;
//            DCurrentPageButtonsFunctions = DGameMenuButtonsFunctions;
//            Changed = true;
//        }
//
//        if (Changed) {
//            DCurrentPageButtonLocations.resize(DCurrentPageButtonsText.size());
//            for (SRectangle Location : DCurrentPageButtonLocations) {
//                Location.DXPosition = 0;
//                Location.DYPosition = 0;
//                Location.DWidth = 0;
//                Location.DHeight = 0;
//            }
//        }
//    }


    /**
     * Fetch the currently selected map.
     * @return DSelectedMap
     */
    public CAssetDecoratedMap DSelectedMap() {
        return DSelectedMap;
    }

    /**
     * Fetch the current map renderer
     * @return DMapRenderer
     */
    public CMapRenderer DMapRenderer() {
        return DMapRenderer;
    }

    /**
     * Fetch the curent asset renderer
     * @return DAssetRenderer
     */
    public CAssetRenderer DAssetRenderer() {
        return DAssetRenderer;
    }

    /**
     * Fetch the current mini map renderer
     * @return DMiniMapRenderer
     */
    public CMiniMapRenderer DMiniMapRenderer() {
        return DMiniMapRenderer;
    }

    /**
     * Called when the 'Single Player Game' button is clicked in the MenuActivity
     */
    public void SinglePlayerGameButtonCallback() {
        ResetPlayerColors();
        DGameSessionType = gstSinglePlayer;
        DCurrentPageButtonHovered = false;
        DSelectedMapOffset = 0;
        DSelectedMapIndex = 0;
        DSelectedMap = DuplicateMap(0, DLoadingPlayerColors);
        DMapRenderer = new CMapRenderer(DTerrainTileset, DSelectedMap);
        DAssetRenderer = new CAssetRenderer(DAssetTilesets, DMarkerTileset, DCorpseTileset,
                DFireTilesets, DBuildingDeathTileset, DArrowTileset, null, DSelectedMap);
        DMiniMapRenderer = new CMiniMapRenderer(DMapRenderer, DAssetRenderer, null, null);

        ChangeMode(gmMapSelect);
    }

    public void MultiPlayerGameButtonCallback() {
        SinglePlayerGameButtonCallback();
        if (DLoggedIn) {
            DMultiplayer.new SendLogOutRequest().execute(DUsername);
            DLoggedIn = false;
        }

        ChangeMode(gmMultiPlayerOptionsMenu);
    }

    public void OnlineMultiPlayerButtonCallback() {

        if (!DLoggedIn) {
            if ((Objects.equals("", DUsername)) || (Objects.equals("", DPassword))) {
                DSetNetworkInfo = true;
//                NetworkOptionsButtonCallback(calldata);
            } else {
                CMultiplayer.SendLoginRequest login = DMultiplayer.new SendLoginRequest();
                login.execute(DUsername, DPassword);
//                while (login.getStatus() != AsyncTask.Status.FINISHED) {
//                    System.out.println("Login AsyncTask Running");
//                }
                boolean validUser = true;
                if (validUser) {
                    System.out.println("validUser");
                    DLoggedIn = true;
                    ChangeMode(gmOnlineMultiPlayerMenu);
                } else {
                    ChangeMode(gmInvalidUser);
                }
            }
        } else if (gstMultiPlayerHost == DGameSessionType) {
            CMultiplayer.DestroyMultiplayerGame destroy = DMultiplayer.new DestroyMultiplayerGame();
            destroy.execute(DUsername, DMultiplayer.getGame().getHost());
            while (destroy.getStatus() != AsyncTask.Status.FINISHED) {
                System.out.println("Destroy MPGame AsyncTask running");
            }
            if (destroy.requestStatus) {
                ChangeMode(gmOnlineMultiPlayerMenu);
            }
        } else {
            ChangeMode(gmOnlineMultiPlayerMenu);
        }
    }

    private void LocalMultiPlayerButtonCallback() {
        ChangeMode(gmLocalMultiPlayerMenu);
    }

    public void OptionsButtonCallback() {
        ChangeMode(gmOptionsMenu);
    }

    public void ExitGameButtonCallback() {
        if (DLoggedIn) {
            CMultiplayer.SendLogOutRequest sendLogOutRequest = DMultiplayer.new SendLogOutRequest();
            sendLogOutRequest.execute(DUsername);

            while (sendLogOutRequest.getStatus() != AsyncTask.Status.FINISHED);

            if (sendLogOutRequest.requestStatus) {
                DLoggedIn = false;
            }
        }

        DDeleted = true;
    }

    public void MainMenuButtonCallback() {
        if (DLoggedIn) {
            CMultiplayer.SendLogOutRequest sendLogOutRequest = DMultiplayer.new SendLogOutRequest();
            sendLogOutRequest.execute(DUsername);

            while (sendLogOutRequest.getStatus() != AsyncTask.Status.FINISHED);

            if (sendLogOutRequest.requestStatus) {
                DLoggedIn = false;
            }
        }

        ChangeMode(gmMainMenu);
    }

    public void GameMenuButtonCallback() {
        ChangeMode(gmGameMenu);
    }

    /**
     * Called when "Sound Options" button is pressed in MenuActivity
     **/
    public void SoundOptionsButtonCallback() {
        /* TODO: Page formatting - probably can be done at the layout
        AppData.DCurrentPageTitle = "Sound Options";
        AppData.DCurrentPageButtonsText.clear();
        AppData.DCurrentPageButtonsFunctions.clear();
        AppData.DCurrentPageButtonLocations.clear();

        AppData.DCurrentPageButtonsText.add("OK");
        AppData.DCurrentPageButtonsFunctions.add(SoundOptionsUpdateButtonCallback);
        AppData.DCurrentPageButtonsText.add("Cancel");
        AppData.DCurrentPageButtonsFunctions.add(OptionsButtonCallback);

        AppData.DOptionsEditSelected = -1;
        AppData.DOptionsEditSelectedCharacter = -1;
        AppData.DOptionsEditLocations.clear();
        AppData.DOptionsEditTitles.clear();
        AppData.DOptionsEditText.clear();
        AppData.DOptionsEditValidationFunctions.clear();
        */

        /* TODO: Sound option settings - will look into
        // Check if file exists, else create and set default values
        if (soundConfFile.is_open()) {
            getline(soundConfFile, lineFromConf);
            colonIndex = lineFromConf.find(":");
            AppData.DSoundVolume = std::stof
            (lineFromConf.substr(colonIndex + 1, lineFromConf.length())) / 100;
            AppData.DOptionsEditTitles.add("FX Volume:");
            AppData.DOptionsEditText.add(lineFromConf.substr(colonIndex + 1, lineFromConf.length()));
            AppData.DOptionsEditValidationFunctions.add(ValidSoundLevelCallback);

            getline(soundConfFile, lineFromConf);
            colonIndex = lineFromConf.find(":");
            AppData.DMusicVolume = std::stof
            (lineFromConf.substr(colonIndex + 1, lineFromConf.length())) / 100;
            AppData.DOptionsEditTitles.add("Music Volume:");
            AppData.DOptionsEditText.add(lineFromConf.substr(colonIndex + 1, lineFromConf.length()));
            AppData.DOptionsEditValidationFunctions.add(ValidSoundLevelCallback);
            soundConfFile.close();
        } else {
            Log.d("DEBUG_LOW",  "No sound configuration file, generating default values.\n");
            soundConfFile.open("../conf/sound.conf", std::fstream::out);
            soundConfFile << "sound:100\nmusic:50";
            soundConfFile.close();
            AppData.DSoundVolume = 1.0;
            AppData.DOptionsEditTitles.add("FX Volume:");
            AppData.DOptionsEditText.add("100");
            AppData.DOptionsEditValidationFunctions.add(ValidSoundLevelCallback);

            AppData.DMusicVolume = 0.5;
            AppData.DOptionsEditTitles.add("Music Volume:");
            AppData.DOptionsEditText.add("50");
            AppData.DOptionsEditValidationFunctions.add(ValidSoundLevelCallback);
        */
        ChangeMode(EGameMode.gmSoundOptions);
    }
//
//    /**
//     * Called when "ok" button pressed.
//     **/
//    static void SoundOptionsUpdateButtonCallback(void*calldata) {
//        CApplicationData AppData = new CApplicationData(calldata);
//        std::fstream soundConfFile;
//        String lineFromConf;
//
//        for (int Index = 0; Index < AppData.DOptionsEditText.size(); Index++) {
//            if (!AppData.DOptionsEditValidationFunctions[Index] (AppData.DOptionsEditText[Index])) {
//                return;
//            }
//        }
//
//        // TODO: Abstract this to more easily allow more config options
//        soundConfFile.open("../conf/sound.conf", std::fstream::trunc | std::fstream::out);
//        soundConfFile << "sound:" + AppData.DOptionsEditText[0] + "\n";
//        soundConfFile << "music:" + AppData.DOptionsEditText[1] + "\n";
//        soundConfFile.close();
//
//        AppData.DSoundVolume = std::stod (AppData.DOptionsEditText[0]) / 100.0;
//        AppData.DMusicVolume = std::stod (AppData.DOptionsEditText[1]) / 100.0;
//        AppData.DSoundLibraryMixer.PlaySong(AppData.DSoundLibraryMixer.FindSong("menu"), AppData.DMusicVolume);
//
//        AppData.ChangeMode(gmOptionsMenu);
//    }

    void NetworkOptionsButtonCallback() {

//        std::fstream networkConfFile("../conf/network.conf", std::fstream::in);
//        String lineFromConf;
//        std::size_t colonIndex;
//
//        AppData.DCurrentPageTitle = "Network Options";
//        AppData.DCurrentPageButtonsText.clear();
//        AppData.DCurrentPageButtonsFunctions.clear();
//        AppData.DCurrentPageButtonLocations.clear();
//
//        AppData.DCurrentPageButtonsText.add("OK");
//        AppData.DCurrentPageButtonsFunctions.add(NetworkOptionsUpdateButtonCallback);
//        AppData.DCurrentPageButtonsText.add("Cancel");
//        AppData.DCurrentPageButtonsFunctions.add(OptionsButtonCallback);
//
//        AppData.DOptionsEditSelected = -1;
//        AppData.DOptionsEditSelectedCharacter = -1;
//        AppData.DOptionsEditLocations.clear();
//        AppData.DOptionsEditTitles.clear();
//        AppData.DOptionsEditText.clear();
//        AppData.DOptionsEditValidationFunctions.clear();
//
//
//        // Check if file exists, else create and set default values
//        if (networkConfFile.is_open()) {
//            getline(networkConfFile, lineFromConf);
//            colonIndex = lineFromConf.find(":");
//            AppData.DUsername = (lineFromConf.substr(colonIndex + 1, lineFromConf.length()));
//            AppData.DOptionsEditTitles.add("User Name:");
//            AppData.DOptionsEditText.add(AppData.DUsername);
//            AppData.DOptionsEditValidationFunctions.add(ValidHostnameCallback);
//
//            getline(networkConfFile, lineFromConf);
//            colonIndex = lineFromConf.find(":");
//            AppData.DPassword = (lineFromConf.substr(colonIndex + 1, lineFromConf.length()));
//            AppData.DOptionsEditTitles.add("Password:");
//            AppData.DOptionsEditText.add(AppData.DPassword);
//            AppData.DOptionsEditValidationFunctions.add(ValidHostnameCallback);
//            getline(networkConfFile, lineFromConf);
//            colonIndex = lineFromConf.find(":");
//            AppData.DRemoteHostname = (lineFromConf.substr(colonIndex + 1, lineFromConf.length()));
//            AppData.DOptionsEditTitles.add("Remote Hostname:");
//            AppData.DOptionsEditText.add(AppData.DRemoteHostname);
//            AppData.DOptionsEditValidationFunctions.add(ValidHostnameCallback);
//
//            getline(networkConfFile, lineFromConf);
//            colonIndex = lineFromConf.find(":");
//            AppData.DMultiplayerPort = std::stoi
//            (lineFromConf.substr(colonIndex + 1, lineFromConf.length()));
//            AppData.DOptionsEditTitles.add("Port Number:");
//            AppData.DOptionsEditText.add(std::to_string (AppData.DMultiplayerPort));
//            AppData.DOptionsEditValidationFunctions.add(ValidPortNumberCallback);
//
//            networkConfFile.close();
//        } else {
//            Log.d("DEBUG_LOW",  "No network configuration file, generating default values.\n");
//            networkConfFile.open("../conf/network.conf", std::fstream::out);
//            networkConfFile << "User Name:user\nPassword:password\nRemote Hostname:localhost\nPort Number:55107";
//            networkConfFile.close();
//
//            AppData.DOptionsEditTitles.add("User Name:");
//            AppData.DOptionsEditText.add(AppData.DUsername);
//            AppData.DOptionsEditValidationFunctions.add(ValidHostnameCallback);
//            AppData.DOptionsEditTitles.add("Password:");
//            AppData.DOptionsEditText.add(AppData.DPassword);
//            AppData.DOptionsEditValidationFunctions.add(ValidHostnameCallback);
//
//            AppData.DOptionsEditTitles.add("Remote Hostname:");
//            AppData.DOptionsEditText.add(AppData.DRemoteHostname);
//            AppData.DOptionsEditValidationFunctions.add(ValidHostnameCallback);
//
//            AppData.DOptionsEditTitles.add("Port Number:");
//            AppData.DOptionsEditText.add(std::to_string (AppData.DMultiplayerPort));
//            AppData.DOptionsEditValidationFunctions.add(ValidPortNumberCallback);
//        }
//
//        AppData.ChangeMode(gmNetworkOptions);
    }

    static void NetworkOptionsUpdateButtonCallback() {
//        CApplicationData AppData = new CApplicationData(calldata);
//        std::fstream networkConfFile;
//
//        networkConfFile.open("../conf/network.conf", std::fstream::trunc | std::fstream::out);
//        networkConfFile << "User Name:" + AppData.DOptionsEditText[0] + "\n";
//        networkConfFile << "Password:" + AppData.DOptionsEditText[1] + "\n";
//        networkConfFile << "Remote Hostname:" + AppData.DOptionsEditText[2] + "\n";
//        networkConfFile << "Port Number:" + AppData.DOptionsEditText[3] + "\n";
//
//        networkConfFile.close();
//
//        // TODO: Now use these inputs to change network connection settings
//
//        AppData.ChangeMode(gmOptionsMenu);
    }

    public void HostMultiPlayerButtonCallback() {
        ResetPlayerColors();
        DCurrentPageButtonHovered = false;
        DSelectedMapOffset = 0;
        DSelectedMapIndex = 0;
        DSelectedMap = DuplicateMap(0, DLoadingPlayerColors);
        DMapRenderer = new CMapRenderer(DTerrainTileset, DSelectedMap);
        DAssetRenderer = new CAssetRenderer(DAssetTilesets, DMarkerTileset, DCorpseTileset,
                DFireTilesets, DBuildingDeathTileset, DArrowTileset, null, DSelectedMap);
//        DMiniMapRenderer = new CMiniMapRenderer(DMapRenderer, DAssetRenderer, null, null,
//                gdk_drawable_get_depth(AppData->DDrawingArea->window) );
        DInGameFlag = true;
        ChangeMode(gmMapSelect);
    }

    void LANHostButtonCallback() {
        ChangeMode(gmLANHostInfo);
    }

    public void JoinMultiPlayerButtonCallback() {
        DGameSessionType = gstMultiPlayerClient;
        DSelectedMapOffset = 0;
        DSelectedMapIndex = 0;
        DSelectedMap = DuplicateMap(0, DLoadingPlayerColors);

        if (DLoggedIn) {
            if (!DLobbyNames.isEmpty()) {
                DLobbyNames.clear();
            }
            CMultiplayer.GetOnlineGames getGames = DMultiplayer.new GetOnlineGames();
            getGames.execute(DUsername);
            while (getGames.getStatus() != AsyncTask.Status.FINISHED);
            ArrayList<Game> games = getGames.games;
            if (games.size() != 0) {
                for (Game game : games) {
                    DLobbyNames.add(game.host());
                }
                DSelectedGameName = "charles";
            } else {
                return;
            }
        }
        Log.i("CalculateLobbyMode", "Multiplayer Game Start");
        DInGameFlag = true;
        LoadGameMap(DSelectedMapIndex);
        ChangeMode(gmBattle);
//        DSoundLibraryMixer.PlaySong(DSoundLibraryMixer.FindSong("game1"), DMusicVolume);

//        ChangeMode(gmHostSelect);
    }

    public void JoinGameLobbyCallback() {
        if (DLoggedIn) {
            Log.i("JoinGameLobbyCallback", "JoinGameLobbyCallback: Joining Game");
            CMultiplayer.JoinMultiplayerGame join = DMultiplayer.new JoinMultiplayerGame();
            join.execute(DUsername, DSelectedGameName);
            while (join.getStatus() != AsyncTask.Status.FINISHED);
            if (join.requestStatus) {
                Log.i("JoinGameLobbyCallback", "Joined Game Lobby");
                //Set the selected map
                DSelectedMap = CAssetDecoratedMap.GetMap(CAssetDecoratedMap.FindMapIndex
                        (DMultiplayer.getGame().getMapName()));
                DSelectedMapIndex = CAssetDecoratedMap.FindMapIndex
                        (DMultiplayer.getGame().getMapName());
                //Set players info
                for (int index = 0; index < DMultiplayer.getGame().getNumPlayers(); index++) {
                    CMultiplayer.PlayerInfo player = DMultiplayer.getGame().getPlayers().get(index);
                    if (Objects.equals(DUsername, player.name)) {
                        DPlayerColor = EPlayerColor.values()[player.color];
                        DPlayerStatus = player.ready == 1;
                    }
                    DLoadingPlayerColors[index + 1] = EPlayerColor.values()[player.color];
                    DLoadingPlayerStatus[index + 1] = player.ready == 1;
                    DLoadingPlayerTypes[index + 1] = EPlayerType.values()[player.type];
                }
                ChangeMode(gmMultiPlayerLobby);
            } else {
                Log.e("JoinGameLobbyCallback", "Failed to Join Game");
                ChangeMode(gmOnlineMultiPlayerMenu);
            }
        } else {
            ChangeMode(gmOnlineMultiPlayerMenu);
        }
    }

    public void ReadyButtonCallback() {
        if (DReady) {
            DReady = false;
            CMultiplayer.StatusChangeRequest change = DMultiplayer.new StatusChangeRequest(PlayerStatus.NotReady);
            change.execute(DMultiplayer.getGame().getHost(), DUsername);
        } else {
            DReady = true;
            CMultiplayer.StatusChangeRequest change = DMultiplayer.new StatusChangeRequest(PlayerStatus.Ready);
            change.execute(DMultiplayer.getGame().getHost(), DUsername);
        }
    }

    public void ChangeTypeButtonCallback() {
        CMultiplayer.AItoHumanRequest request = DMultiplayer.new AItoHumanRequest();
        request.execute(DMultiplayer.getGame().getHost(), DPlayerNameRequestTypeChange);
    }

    public void SelectMapButtonCallback() throws InterruptedException {

        for (int Index = 0; Index < pcMax.ordinal(); Index++) {
            DLoadingPlayerTypes[Index] = ptNone;
            if (Index != 0) {
                if (1 == Index) {
                    DLoadingPlayerTypes[Index] = ptHuman;
                } else if (Index <= DSelectedMap.PlayerCount()) {
                    DLoadingPlayerTypes[Index] = gstMultiPlayerHost == DGameSessionType ?
                            ptHuman : ptAIEasy;
                }
            }
        }

        if (DLoggedIn) {
            CMultiplayer.CreateMultiplayerGame create = DMultiplayer.new CreateMultiplayerGame();
            create.execute(DUsername, ((Integer) DSelectedMap.PlayerCount()).toString(), DSelectedMap.MapName());
//            while (create.getStatus() != AsyncTask.Status.FINISHED);
            Thread.sleep(3000);

            create.requestStatus = true;
            if (create.requestStatus) {

                if (create.game == null) {
                    System.out.println("game null");
                }
                DMultiplayer.game = DMultiplayer.new GameInfo(create.game, create
                        .gameConstants);

                DGameSessionType = gstMultiPlayerHost;
                System.out.println("success");
                Log.i("SelectMapButtonCallback", "Game Constants player count " +
                        DMultiplayer.getGame().getNumPlayers());
                DLoadingPlayerColors[0] = pcNone;

                for (int index = 0; index < DMultiplayer.getGame().getNumPlayers(); index++) {
                    CMultiplayer.PlayerInfo player = DMultiplayer.getGame().getPlayers().get(index);
                    if (0 == index) {
                        DPlayerColor = EPlayerColor.values()[player.color];
                        DPlayerStatus = player.ready == 1;
                    }
                    DLoadingPlayerColors[index + 1] = EPlayerColor.values()[player.color];
                    DLoadingPlayerStatus[index + 1] = player.ready == 1;
                    DLoadingPlayerTypes[index + 1] = EPlayerType.values()[player.type];
                }
                Log.i("SelectMapButtonCallback", "Set Loading Player vectors with new GameInfo");
                Thread.sleep(1000);
                ReadyButtonCallback();
                Thread.sleep(1000);
                ChangeMode(gmMultiPlayerLobby);
            } else {
                Log.i("SelectMapButtonCallback", "Failed to create multiplayer game");
                ChangeMode(gmOnlineMultiPlayerMenu);
            }
        } else {
            DPlayerColor = EPlayerColor.values()[1];
            ChangeMode(gmPlayerAISelect);
        }
    }

    public void PlayGameButtonCallback() {
        if (gstMultiPlayerHost == DGameSessionType) {
            DMultiplayer.new StartGameRequest().execute(DUsername);
            DInGameFlag = true;
            DPlayerColor = DLoadingPlayerColors[DPlayerColor.ordinal()];
            LoadGameMap(DSelectedMapIndex);
            System.out.println("load game map");
            ChangeMode(gmBattle);
            DSoundLibraryMixer.PlaySong(DSoundLibraryMixer.FindSong("game1"), DMusicVolume);
            ChangeMode(gmBattle);
        } else {
            DInGameFlag = true;
            DPlayerColor = DLoadingPlayerColors[DPlayerColor.ordinal()];
            LoadGameMap(DSelectedMapIndex);
            System.out.println("load game map");
            ChangeMode(gmBattle);
            DSoundLibraryMixer.PlaySong(DSoundLibraryMixer.FindSong("game1"), DMusicVolume);
        }
    }

    void ResumeGameButtonCallback() {
        ChangeMode(gmBattle);
    }

    void ExitToMainMenuButtonCallback() {
        DInGameFlag = false;
        ChangeMode(gmMainMenu);
    }

//    static boolean ValidSoundLevelCallback(String text) {
//        try {
//            int Level = std::stoi (text);
//            if ((0 <= Level) && (100 >= Level)) {
//                return text == std::to_string (Level);
//            }
//        } catch (std::exception E) {
//            return false;
//        }
//        return false;
//    }

    static boolean ValidHostnameCallback(String text) {
        int CharSinceDot = 0;

        if (253 < text.length()) {
            return false;
        }
        if (0 == text.length()) {
            return false;
        }
        for (char Char : text.toCharArray()) {
            if ('.' == Char) {
                if (0 == CharSinceDot) {
                    return false;
                }
                CharSinceDot = 0;
            } else {
                CharSinceDot++;
                if (63 < CharSinceDot) {
                    return false;
                }
                if (('-' != Char) && (!(('0' <= Char) && ('9' >= Char))) && (!(('a' <= Char) &&
                        ('z' >= Char))) && (!(('A' <= Char) && ('Z' >= Char)))) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean ValidPortNumberCallback(String text) {
        try {
            int Port = Integer.parseInt(text);
            if ((1024 < Port) && (65535 >= Port)) {
                return text.equals(String.valueOf(Port));
            }
        } catch (Exception E) {
            return false;
        }
        return false;
    }

    private void ResetPlayerColors() {
        for (int Index = 0; Index < pcMax.ordinal(); Index++)
            DLoadingPlayerColors[Index] = EPlayerColor.values()[Index];
    }

    private void ResetMap() {
        DNextGameMode = gmBattle;
    }

    private void LoadGameMap(int index) {
        int DetailedMapWidth, DetailedMapHeight;
        DGameModel = new CGameModel(index, 7, DLoadingPlayerColors);
        for (int Index = 1; Index < CGameDataTypes.EPlayerColor.pcMax.ordinal(); Index++) {
            DGameModel.Player(DPlayerColor).IsAI((EPlayerType.ptAIEasy.ordinal() <=
                    DLoadingPlayerTypes[Index].ordinal()) && (EPlayerType.ptAIHard.ordinal() >=
                    DLoadingPlayerTypes[Index].ordinal()));
        }
        for (int Index = 1; Index < pcMax.ordinal(); Index++) {
            if (DGameModel.Player(CGameDataTypes.EPlayerColor.values()[Index]).IsAI()) {
                int Downsample;
                switch (DLoadingPlayerTypes[Index]) {
                    case ptAIEasy:
                        Downsample = CPlayerAsset.UpdateFrequency();
                        break;
                    case ptAIMedium:
                        Downsample = CPlayerAsset.UpdateFrequency() / 2;
                        break;
                    default:
                        Downsample = CPlayerAsset.UpdateFrequency() / 4;
                        break;
                }
                DAIPlayers.set(Index, new CAIPlayer(DGameModel.Player(CGameDataTypes.EPlayerColor.values()[Index]), Downsample));
            }
            DCurrentAssetCapability[Index] = actNone;
        }

        DetailedMapWidth = DGameModel.Map().Width() * DTerrainTileset.TileWidth();
        DetailedMapHeight = DGameModel.Map().Height() * DTerrainTileset.TileHeight();

        ResetMap();

        DMapRenderer = new CMapRenderer(DTerrainTileset, DGameModel.Player(DPlayerColor).DPlayerMap);
        DAssetRenderer = new CAssetRenderer(DAssetTilesets, DMarkerTileset, DCorpseTileset,
                DFireTilesets, DBuildingDeathTileset, DArrowTileset, DGameModel.Player
                (DPlayerColor), DGameModel.Player(DPlayerColor).DPlayerMap);
        DFogRenderer = new CFogRenderer(DFogTileset, DGameModel.Player(DPlayerColor).VisibilityMap());
        DViewportRenderer = new CViewportRenderer(DMapRenderer, DAssetRenderer, DFogRenderer);
        DMiniMapRenderer = new CMiniMapRenderer(DMapRenderer, DAssetRenderer, DFogRenderer, DViewportRenderer);
        DUnitDescriptionRenderer = new CUnitDescriptionRenderer(DGameModel.Player(DPlayerColor).DPlayerMap);
        DUnitActionRenderer = new CUnitActionRenderer(DGameModel.Player(DPlayerColor).DActualMap, DMapRenderer);
//
//        DResourceRenderer = new CResourceRenderer(DMiniIconTileset, DFonts[CUnitDescriptionRenderer::fsMedium], DGameModel.Player
//                (DPlayerColor));

        DSoundEventRenderer = new CSoundEventRenderer(DSoundLibraryMixer, DGameModel.Player(DPlayerColor));
//        DMenuButtonRenderer = new CButtonRenderer(DButtonColorTileset, DInnerBevel, DOuterBevel,
//                DFonts[CUnitDescriptionRenderer::fsMedium]);
//
//        DMenuButtonRenderer.Text("Menu");
//        DMenuButtonRenderer.ButtonColor(DPlayerColor);

//        GdkGeometry Geometry;
//        int LeftPanelWidth = Math.max(DUnitDescriptionRenderer.MinimumWidth(), DUnitActionRenderer
//                .MinimumWidth()) + DOuterBevel.Width() * 4;
//        LeftPanelWidth = Math.max(LeftPanelWidth, MINI_MAP_MIN_WIDTH + DInnerBevel.Width() * 4);
//        int MinUnitDescrHeight;

//        DMiniMapXOffset = DInnerBevel.Width() * 2;
//        DUnitDescriptionXOffset = DOuterBevel.Width() * 2;
//        DUnitActionXOffset = DUnitDescriptionXOffset;
//        DViewportXOffset = LeftPanelWidth + DInnerBevel.Width();
//
//        DMiniMapYOffset = DBorderWidth;
//        DUnitDescriptionYOffset = DMiniMapYOffset + (LeftPanelWidth - DInnerBevel.Width() * 3) + DOuterBevel.Width() * 2;
//        MinUnitDescrHeight = DUnitDescriptionRenderer.MinimumHeight(LeftPanelWidth - DOuterBevel.Width() * 4, 9);
//        DUnitActionYOffset = DUnitDescriptionYOffset + MinUnitDescrHeight + DOuterBevel.Width() * 3;
//        DViewportYOffset = DBorderWidth;

//        Geometry.min_width = INITIAL_MAP_WIDTH;
//        Geometry.min_height = DUnitDescriptionYOffset + MinUnitDescrHeight + DUnitActionRenderer.MinimumHeight() + DOuterBevel.Width() * 5;
//        Geometry.max_width = DViewportXOffset + DetailedMapWidth + DBorderWidth;
//        Geometry.max_height = MAX(Geometry.min_height, DetailedMapHeight + DBorderWidth * 2);
//        gtk_window_set_geometry_hints(GTK_WINDOW(DMainWindow), DDrawingArea, Geometry, (GdkWindowHints) (GDK_HINT_MIN_SIZE |
//                GDK_HINT_MAX_SIZE));
//
//        ResizeCanvases();

//        DMenuButtonRenderer.Width(DViewportXOffset / 2);
//        DMenuButtonXOffset = DViewportXOffset / 2 - DMenuButtonRenderer.Width() / 2;
//        DMenuButtonYOffset = (DViewportYOffset - DOuterBevel.Width()) / 2 - DMenuButtonRenderer.Height() / 2;
//
//        int CurWidth, CurHeight;
//
//        gdk_pixmap_get_size(DViewportPixmap, & CurWidth,&CurHeight);
//        DViewportRenderer.InitViewportDimensions(CurWidth, CurHeight);

//        for (auto WeakAsset : DGameModel.Player(DPlayerColor).Assets()) {
//            if (auto Asset = WeakAsset.lock()) {
//                DViewportRenderer.CenterViewport(Asset.Position());
//                break;
//            }
//        }
    }

//    public void ResizeCanvases() {
//        GtkAllocation DrawingAreaAllocation;
//        int ViewportWidth, ViewportHeight;
//        int UserDescrWidth, UserDescrHeight;
//        // Resize the canvas
//        if (!DDrawingContext || !DDrawingArea) {
//            return;
//        }
//
//        gtk_widget_get_allocation(DDrawingArea, & DrawingAreaAllocation);
//        Log.d("DEBUG_LOW",  "Resizing %d x %d\n", DrawingAreaAllocation.width, DrawingAreaAllocation.height);
//        ViewportWidth = DrawingAreaAllocation.width - DViewportXOffset - DBorderWidth;
//        ViewportHeight = DrawingAreaAllocation.height - DViewportYOffset - DBorderWidth;
//        if (null != DDoubleBufferPixmap) {
//            int CurWidth, CurHeight;
//
//            gdk_pixmap_get_size(DDoubleBufferPixmap, CurWidth, CurHeight);
//            if ((DrawingAreaAllocation.width != CurWidth) || (DrawingAreaAllocation.height != CurHeight)) {
//                g_object_unref(DDoubleBufferPixmap);
//                DDoubleBufferPixmap = null;
//            }
//        }
//        if (null == DDoubleBufferPixmap) {
//            DDoubleBufferPixmap = gdk_pixmap_new(DDrawingArea.window, DrawingAreaAllocation.width, DrawingAreaAllocation.height, -1);
//        }
//        if (null != DWorkingBufferPixmap) {
//            int CurWidth, CurHeight;
//
//            gdk_pixmap_get_size(DWorkingBufferPixmap, & CurWidth,&CurHeight);
//            if ((DrawingAreaAllocation.width != CurWidth) || (DrawingAreaAllocation.height != CurHeight)) {
//                g_object_unref(DWorkingBufferPixmap);
//                DWorkingBufferPixmap = null;
//            }
//        }
//        if (null == DWorkingBufferPixmap) {
//            DWorkingBufferPixmap = gdk_pixmap_new(DDrawingArea.window, DrawingAreaAllocation.width, DrawingAreaAllocation.height, -1);
//        }
//        if (null != DWorkingPixbuf) {
//            g_object_unref(DWorkingPixbuf);
//            DWorkingPixbuf = null;
//        }
//        if (null == DMiniMapPixmap) {
//            int Dimension = DViewportXOffset - DInnerBevel.Width() * 5;
//            DMiniMapPixmap = gdk_pixmap_new(DDrawingArea.window, Dimension, Dimension, -1);
//        }
//        if (null != DMiniMapPixmap) {
//            GdkColor ColorBlack = {0, 0, 0, 0};
//            int CurWidth, CurHeight;
//
//            gdk_pixmap_get_size(DMiniMapPixmap, CurWidth, CurHeight);
//
//            gdk_gc_set_rgb_fg_color(DDrawingContext, ColorBlack);
//            gdk_gc_set_rgb_bg_color(DDrawingContext, ColorBlack);
//            gdk_draw_rectangle(DMiniMapPixmap, DDrawingContext, true, 0, 0, CurWidth, CurHeight);
//        }
//
//        UserDescrWidth = DViewportXOffset - DInnerBevel.Width() - DOuterBevel.Width() * 4;
//        UserDescrHeight = DUnitDescriptionRenderer.MinimumHeight(UserDescrWidth, 9);
//
//        if (null != DUnitDescriptionPixmap) {
//            int CurWidth, CurHeight;
//
//            gdk_pixmap_get_size(DUnitDescriptionPixmap, & CurWidth,&CurHeight);
//            if ((CurWidth != UserDescrWidth) || (CurHeight != UserDescrHeight)) {
//                g_object_unref(DUnitDescriptionPixmap);
//                DUnitDescriptionPixmap = null;
//            }
//        }
//        if (null == DUnitDescriptionPixmap) {
//            DUnitDescriptionPixmap = gdk_pixmap_new(DDrawingArea.window, UserDescrWidth, UserDescrHeight, -1);
//        }
//        DUnitActionYOffset = DUnitDescriptionYOffset + UserDescrHeight + DOuterBevel.Width() * 3;
//
//        if (null != DUnitActionPixmap) {
//            int CurWidth, CurHeight;
//
//            gdk_pixmap_get_size(DUnitActionPixmap, CurWidth, CurHeight);
//            if ((CurWidth != DUnitActionRenderer.MinimumWidth()) || (CurHeight != DUnitActionRenderer.MinimumHeight())) {
//                g_object_unref(DUnitActionPixmap);
//                DUnitActionPixmap = null;
//            }
//        }
//        if (null == DUnitActionPixmap) {
//            DUnitActionPixmap = gdk_pixmap_new(DDrawingArea.window, DUnitActionRenderer.MinimumWidth(), DUnitActionRenderer.MinimumHeight
//                    (), -1);
//        }
//        if (null != DResourcePixmap) {
//            int CurWidth, CurHeight;
//
//            gdk_pixmap_get_size(DResourcePixmap, CurWidth, CurHeight);
//
//            if ((ViewportWidth != CurWidth) || (DBorderWidth != CurHeight)) {
//                g_object_unref(DResourcePixmap);
//                DResourcePixmap = null;
//            }
//        }
//        if (null == DResourcePixmap) {
//            DResourcePixmap = gdk_pixmap_new(DDrawingArea.window, ViewportWidth, DBorderWidth, -1);
//        }
//
//        if (null != DViewportPixmap) {
//            int CurWidth, CurHeight;
//
//            gdk_pixmap_get_size(DViewportPixmap, & CurWidth, CurHeight);
//
//            if ((ViewportWidth != CurWidth) || (ViewportHeight != CurHeight)) {
//                g_object_unref(DViewportPixmap);
//                DViewportPixmap = null;
//                g_object_unref(DViewportTypePixmap);
//                DViewportTypePixmap = null;
//            }
//        }
//        if (null == DViewportPixmap) {
//            DViewportPixmap = DGameView;
//            GdkColor ColorBlack = {0, 0, 0, 0};
//
//            if (0 > ViewportWidth) {
//                ViewportWidth = 1;
//            }
//            if (0 > ViewportHeight) {
//                ViewportHeight = 1;
//            }
//            DViewportPixmap = gdk_pixmap_new(DDrawingArea.window, ViewportWidth, ViewportHeight, -1);
//            gdk_gc_set_rgb_fg_color(DDrawingContext, ColorBlack);
//            gdk_gc_set_rgb_bg_color(DDrawingContext, ColorBlack);
//            gdk_draw_rectangle(DViewportPixmap, DDrawingContext, true, 0, 0, ViewportWidth, ViewportHeight);
//            DViewportTypePixmap = gdk_pixmap_new(DDrawingArea.window, ViewportWidth, ViewportHeight, -1);
//
//        }
//    }
}