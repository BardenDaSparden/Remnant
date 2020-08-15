package com.remnant.gamestate;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_3;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_4;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DELETE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F5;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_G;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_N;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_O;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_TAB;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;

import java.util.ArrayList;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.remnant.assets.Texture;
import com.remnant.engine.Engine;
import com.remnant.graphics.BlendState;
import com.remnant.graphics.Camera;
import com.remnant.graphics.CameraStack;
import com.remnant.graphics.Light;
import com.remnant.graphics.Mesh;
import com.remnant.graphics.TypedUIRenderer;
import com.remnant.graphics.Vertex;
import com.remnant.input.Keyboard;
import com.remnant.input.KeyboardListener;
import com.remnant.input.Mouse;
import com.remnant.input.MouseListener;
import com.remnant.map.Collider;
import com.remnant.map.ColliderDefinition;
import com.remnant.map.LightDefinition;
import com.remnant.map.Map;
import com.remnant.map.MapIO;
import com.remnant.map.MapRenderer;
import com.remnant.map.Pickup;
import com.remnant.map.PickupDefinition;
import com.remnant.map.Terrain;
import com.remnant.map.TerrainDefinition;
import com.remnant.math.Color;
import com.remnant.math.Math2D;
import com.remnant.math.Transform;
import com.remnant.player.Player;
import com.remnant.player.PlayerCamera;
import com.remnant.player.PlayerController;
import com.remnant.player.PlayerFactory;
import com.remnant.player.PlayerRenderer;
import com.remnant.ui.AnchorLayout;
import com.remnant.ui.AnchorPane;
import com.remnant.ui.ColliderOptionsDialog;
import com.remnant.ui.CreateMapData;
import com.remnant.ui.CreateMapDialog;
import com.remnant.ui.Cursor;
import com.remnant.ui.DebugUISceneRenderer;
import com.remnant.ui.EditorProperties;
import com.remnant.ui.GridOptionsDialog;
import com.remnant.ui.GridOverlay;
import com.remnant.ui.Label;
import com.remnant.ui.LightOptionsDialog;
import com.remnant.ui.OpenMapData;
import com.remnant.ui.OpenMapDialog;
import com.remnant.ui.PickupOptionsDialog;
import com.remnant.ui.SaveMapData;
import com.remnant.ui.SaveMapDialog;
import com.remnant.ui.SpawnOptionsDialog;
import com.remnant.ui.TerrainOptionsDialog;
import com.remnant.ui.UINode;
import com.remnant.ui.UIStage;
import com.remnant.ui.UIStyle;
import com.remnant.ui.WindowData;
import com.remnant.ui.WindowListener;

public class EditorState extends GameState {

	public EditorState(Engine engine) {
		super(engine);
	}
	
	public static final int NEW_MAP = GLFW_KEY_N;
	public static final int OPEN_MAP = GLFW_KEY_O;
	public static final int CLOSE_MAP = GLFW_KEY_C;
	public static final int SAVE_MAP = GLFW_KEY_S;
	public static final int RETURN_TO_MENU = GLFW_KEY_ESCAPE;
	
	public static final int TOGGLE_EDIT_MODE = GLFW_KEY_TAB;
	public static final int TOGGLE_MODE = GLFW_KEY_F5;
	public static final int DELETE_OBJECT = GLFW_KEY_DELETE;
	
	public static final int SHOW_OBJECT_PROPERTIES = GLFW_KEY_P;
	
	public static final int SET_CREATE_MODE_TERRAIN = GLFW_KEY_1;
	public static final int SET_CREATE_MODE_LIGHTS = GLFW_KEY_2;
	public static final int SET_CREATE_MODE_SPAWNS = GLFW_KEY_3;
	public static final int SET_CREATE_MODE_COLLIDERS = GLFW_KEY_4;
	public static final int SET_CREATE_MODE_PICKUPS = GLFW_KEY_5;
	
	public static final int SHOW_GRID_OPTIONS = GLFW_KEY_G;
	
	public static final int EDIT_MODE_PICK = 1;
	public static final int EDIT_MODE_CREATE = 2;
	
	public static final int CREATE_MODE_TERRAIN = 1;
	public static final int CREATE_MODE_LIGHTS = 2;
	public static final int CREATE_MODE_SPAWNS = 3;
	public static final int CREATE_MODE_COLLIDERS = 4;
	public static final int CREATE_MODE_PICKUPS = 5;
	
	public static final int MODE_EDITING = 1;
	public static final int MODE_PLAYING = 2;
	
	KeyboardListener keyboardListener;
	MouseListener mouseListener;
	Vector2f mousePos = new Vector2f();
	boolean isMMBDown = false;
	
	UIStage mainUIStage;
	AnchorPane root;
	
	Label mapName;
	Label editModeLabel;
	Label createModeLabel;
	
	CreateMapDialog newMapDialog;
	OpenMapDialog openMapDialog;
	SaveMapDialog saveMapDialog;
	
	//TODO Change to EditorProperties and add EditorCamera settings export / import
	EditorProperties editorProperties;
	GridOptionsDialog gridOptionsDialog;
	
	TerrainDefinition terrainProperties;
	LightDefinition lightProperties;
	//TODO SpawnDefinition spawnProperties;
	ColliderDefinition colliderProperties;
	PickupDefinition pickupProperties;
	
	TerrainOptionsDialog terrainOptionsDialog;
	LightOptionsDialog lightOptionsDialog;
	SpawnOptionsDialog spawnOptionsDialog;
	ColliderOptionsDialog colliderOptionsDialog;
	PickupOptionsDialog pickupOptionsDialog;
	
	EditorCamera editorCamera;
	
	//TODO refactor GridOverlay into seperate classes for logic and rendering
	GridOverlay grid;
	
	Map map = null;
	
	int interactionMode = EDIT_MODE_PICK;
	int objectMode = CREATE_MODE_TERRAIN;
	int mode = MODE_EDITING;
	
	MapRenderer mapRenderer;
	
	Object lastSelectedObject = null;
	SelectedObjectRenderer selectedObjectRenderer;
	
	DebugUISceneRenderer debugRenderer;
	
	@Override
	public void load() {
		debugRenderer = new DebugUISceneRenderer(game.width, game.height);
		editorCamera = new EditorCamera(game.width, game.height);
		
		grid = new GridOverlay(8000, 8000);
		
		game.uirenderer.add(grid);
		game.uirenderer.add(debugRenderer);
		
		keyboardListener = new KeyboardListener() {
			
			@Override
			public void onKeyRelease(int key) { }
			
			@Override
			public void onKeyPress(int key) {
				if(mainUIStage.interacting())
					return;
				if(key == OPEN_MAP){
					ArrayList<String> mapNames = new ArrayList<String>();
					MapIO.getAvailableMaps(mapNames);
					openMapDialog.loadDiscoveredMaps(mapNames);
					mainUIStage.showDialog(openMapDialog);
				} else if(key == NEW_MAP){
					mainUIStage.showDialog(newMapDialog);
				} else if(key == SAVE_MAP){
					mainUIStage.showDialog(saveMapDialog);
				} else if(key == CLOSE_MAP){
					closeMap();
				} else if(key == RETURN_TO_MENU){
					game.ui.clear();
					game.setGameState(Engine.MAIN_MENU_STATE);
				} else if(key == SHOW_GRID_OPTIONS){
					mainUIStage.showDialog(gridOptionsDialog);
				} else if(key == TOGGLE_EDIT_MODE){
					toggleEditMode();
				} else if(key == DELETE_OBJECT){
					tryObjectDelete();
				} else if(key == SET_CREATE_MODE_TERRAIN){
					setObjectMode(CREATE_MODE_TERRAIN);
				} else if(key == SET_CREATE_MODE_LIGHTS){
					setObjectMode(CREATE_MODE_LIGHTS);
				} else if(key == SET_CREATE_MODE_SPAWNS){
					setObjectMode(CREATE_MODE_SPAWNS);
				} else if(key == SET_CREATE_MODE_COLLIDERS){
					setObjectMode(CREATE_MODE_COLLIDERS);
				} else if(key == SET_CREATE_MODE_PICKUPS){
					setObjectMode(CREATE_MODE_PICKUPS);
				} else if(key == SHOW_OBJECT_PROPERTIES){
					showProperties();
				}
			}
			
			@Override
			public void onChar(int codepoint) { }
		};
		
		mouseListener = new MouseListener() {
			
			@Override
			public void onScroll(float dy) {
				if(mode == MODE_PLAYING)
					return;
				
				if(hasSelectedObject()){
					if(lastSelectedObject instanceof Terrain){
						Terrain t = (Terrain) lastSelectedObject;
						t.getTransform().rotate((float)Math.signum(dy) * (float)Math.PI / 16f);
					}
				} else {
					if(!mainUIStage.interacting() && isMapOpen())
						editorCamera.zoom(dy);
				}
			}
			
			@Override
			public void onMove(float dx, float dy) {
				if(mode == MODE_PLAYING)
					return;
				
				if(isMMBDown && !mainUIStage.interacting() && isMapOpen())
					editorCamera.move(dx, dy);
			}
			
			@Override
			public void onButtonRelease(int button) {
				if(mode == MODE_PLAYING)
					return;
				
				if(button == Mouse.MIDDLE_BUTTON && !mainUIStage.interacting() && isMapOpen()){
					game.input.getMouse().setCursorPosition(0, 0, game.width, game.height);
					game.cursor.setVisible(true);
				}
			}
			
			@Override
			public void onButtonPress(int button) {
				if(mode == MODE_PLAYING)
					return;
				
				if(button == Mouse.MIDDLE_BUTTON && !mainUIStage.interacting() && isMapOpen()){
					game.cursor.setVisible(false);
				} else if(button == Mouse.LEFT_BUTTON && !mainUIStage.interacting() && isMapOpen()){
					interactLeftClick();
				} else if(button == Mouse.RIGHT_BUTTON && !mainUIStage.interacting() && isMapOpen()){
					interactRightClick();
				}
			}
		};

		Mouse mouse = game.input.getMouse();
		mouse.addListener(mouseListener);
		
		Keyboard keyboard = game.input.getKeyboard();
		keyboard.addListener(keyboardListener);
		
		root = new AnchorPane();
		root.setPrefSize(game.width, game.height);
		root.setSizeMode(UINode.PREFERRED, UINode.PREFERRED);
		
		editorProperties = new EditorProperties();
		terrainProperties = new TerrainDefinition();
		lightProperties = new LightDefinition();
		colliderProperties = new ColliderDefinition();
		pickupProperties = new PickupDefinition();
		
		newMapDialog = new CreateMapDialog();
		newMapDialog.hide();
		newMapDialog.addWindowListener(new WindowListener() {
			@Override
			public void onDataSubmit(WindowData data) {
				if(data instanceof CreateMapData){
					CreateMapData mapData = (CreateMapData) data;
					createNewMap(mapData);
				}
			}

			@Override
			public void onClose() {
				
			}
		});
		
		openMapDialog = new OpenMapDialog();
		openMapDialog.hide();
		openMapDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void onDataSubmit(WindowData data) {
				OpenMapData openData = (OpenMapData) data;
				openMap(openData);
			}
			
			@Override
			public void onClose() {
				
			}
		});
		
		saveMapDialog = new SaveMapDialog();
		saveMapDialog.hide();
		saveMapDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void onDataSubmit(WindowData data) {
				SaveMapData saveData = (SaveMapData) data;
				saveMap(saveData);
			}
			
			@Override
			public void onClose() {
				
			}
		});
		
		gridOptionsDialog = new GridOptionsDialog(editorProperties);
		gridOptionsDialog.hide();
		gridOptionsDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void onDataSubmit(WindowData data) {
				
			}
			
			@Override
			public void onClose() {
				
			}
		});
		
		terrainOptionsDialog = new TerrainOptionsDialog(terrainProperties);
		terrainOptionsDialog.hide();
		terrainOptionsDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void onDataSubmit(WindowData data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				
			}
		});
		
		lightOptionsDialog = new LightOptionsDialog(lightProperties);
		lightOptionsDialog.hide();
		lightOptionsDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void onDataSubmit(WindowData data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				
			}
		});
		
		spawnOptionsDialog = new SpawnOptionsDialog();
		spawnOptionsDialog.hide();
		spawnOptionsDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void onDataSubmit(WindowData data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				
			}
		});
		
		colliderOptionsDialog = new ColliderOptionsDialog(colliderProperties);
		colliderOptionsDialog.hide();
		colliderOptionsDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void onDataSubmit(WindowData data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				
			}
		});
		
		pickupOptionsDialog = new PickupOptionsDialog(pickupProperties);
		pickupOptionsDialog.hide();
		pickupOptionsDialog.addWindowListener(new WindowListener() {
			
			@Override
			public void onDataSubmit(WindowData data) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				
			}
		});
		
		UIStyle labelStyle = new UIStyle();
		labelStyle.getTextColor().set(1, 1, 1, 1);
		
		mapName = new Label("Press \"O\" To Open Map...");
		mapName.setStyle(labelStyle);
		
		editModeLabel = new Label(getEditMode());
		editModeLabel.setStyle(labelStyle);
		editModeLabel.hide();
		
		createModeLabel = new Label(getCreateMode());
		createModeLabel.setStyle(labelStyle);
		createModeLabel.hide();
		
		root.add(mapName, AnchorLayout.TOP);
		root.add(editModeLabel, AnchorLayout.TOP);
		root.add(createModeLabel, AnchorLayout.TOP);
		root.add(terrainOptionsDialog, AnchorLayout.CENTER);
		root.add(lightOptionsDialog, AnchorLayout.CENTER);
		root.add(spawnOptionsDialog, AnchorLayout.CENTER);
		root.add(gridOptionsDialog, AnchorLayout.CENTER);
		root.add(colliderOptionsDialog, AnchorLayout.CENTER);
		root.add(pickupOptionsDialog, AnchorLayout.CENTER);
		root.add(newMapDialog, AnchorLayout.CENTER);
		root.add(openMapDialog, AnchorLayout.CENTER);
		root.add(saveMapDialog, AnchorLayout.CENTER);
		//root.add(new MapObjectView(new TerrainObject(game), root, "Object"), AnchorLayout.CENTER);
		
		mainUIStage = new UIStage(root);
		game.ui.push(mainUIStage);
		
		debugRenderer.setStage(mainUIStage);
		
		mouse.setCursorPosition(0, 0, game.width, game.height);
		mouse.setGrabbed(true);
		game.cursor.setPosition(0, 0);
		editorCamera.reset();
		
		selectedObjectRenderer = new SelectedObjectRenderer();
		game.uirenderer.add(selectedObjectRenderer);
		
		mapRenderer = new MapRenderer();
		game.renderer.add(mapRenderer);
	}
	
	void createNewMap(CreateMapData data){
		closeMap();
		
		System.out.println("[EDITOR]["+ data.mapName +"] Created.");
		
		saveMapDialog.setMapName(data.mapName);
		mapName.setText(data.mapName);
		
		createModeLabel.show();
		editModeLabel.show();
		
		map = new Map(game, data.mapName);
		mapRenderer.setMap(map);
		
		editorCamera.reset();
		map.open();
	}
	
	void openMap(OpenMapData data){
		closeMap();
		System.out.println("[EDITOR]["+ data.mapName +"] Opened.");
		
		saveMapDialog.setMapName(data.mapName);
		mapName.setText(data.mapName);
		
		createModeLabel.show();
		editModeLabel.show();
		
		grid.setDraw(false);
		
		map = new Map(game, data.mapName);
		mapRenderer.setMap(map);
		MapIO.read(data.mapName, map, editorProperties);
		
		gridOptionsDialog.updateProperties();
		
		editorCamera.reset();
		editorCamera.readProperties(editorProperties);
		
		map.open();
	}
	
	void closeMap(){
		if(isMapOpen()){
			System.out.println("[EDITOR]["+ map.getName() +"] Closed.");
			
			saveMapDialog.setMapName("");
			mapName.setText("Press \"O\" To Open Map...");
			
			editorProperties.drawGrid = false;
			editorProperties.snapToGrid = false;
			
			createModeLabel.hide();
			editModeLabel.hide();
			
			setInteractionMode(EDIT_MODE_PICK);
			selectObject(null);
			
			mapName.setText("");
			
			grid.setDraw(false);
			map.unload();
			map.close();
		}
	}
	
	boolean isMapOpen(){
		boolean available = (map != null);
		if(!available)
			return false;
		return available && map.isOpen();
	}
	
	void saveMap(SaveMapData saveData){
		if(isMapOpen() && map.isDirty()){
			System.out.println("[EDITOR]["+ map.getName() +"] Saved.");
			map.setDirty(false);
			editorProperties.cameraZoom.set(editorCamera.zoom);
			editorProperties.cameraPosition.set(editorCamera.getTranslation());
			MapIO.write(map.getName(), map, editorProperties);
		}
	}
	
	String getEditMode(){
		String str = "";
		if(interactionMode == EDIT_MODE_PICK)
			str = "Pick";
		else if(interactionMode == EDIT_MODE_CREATE)
			str = "Create";
		return str;
	}
	
	String getCreateMode(){
		String str = "";
		if(objectMode == CREATE_MODE_TERRAIN)
			str = "Terrain";
		else if(objectMode == CREATE_MODE_LIGHTS)
			str = "Lights";
		else if(objectMode == CREATE_MODE_SPAWNS)
			str = "Spawns";
		else if(objectMode == CREATE_MODE_COLLIDERS)
			str = "Colliders";
		else if(objectMode == CREATE_MODE_PICKUPS)
			str = "Pickups";
		return str;
	}
	
	void showProperties(){
		
		if(!isMapOpen())
			return;
		
		terrainOptionsDialog.close();
		lightOptionsDialog.close();
		spawnOptionsDialog.close();
		colliderOptionsDialog.close();
		pickupOptionsDialog.close();
		
		if(objectMode == CREATE_MODE_TERRAIN){
			mainUIStage.showDialog(terrainOptionsDialog);
		} else if(objectMode == CREATE_MODE_LIGHTS){
			mainUIStage.showDialog(lightOptionsDialog);
		} else if(objectMode == CREATE_MODE_SPAWNS){
			mainUIStage.showDialog(spawnOptionsDialog);
		} else if(objectMode == CREATE_MODE_COLLIDERS){
			mainUIStage.showDialog(colliderOptionsDialog);
		} else if(objectMode == CREATE_MODE_PICKUPS){
			mainUIStage.showDialog(pickupOptionsDialog);
		}
	}
	
	void toggleEditMode(){
		selectObject(null);
		if(interactionMode == EDIT_MODE_PICK)
			setInteractionMode(EDIT_MODE_CREATE);
		else if(interactionMode == EDIT_MODE_CREATE)
			setInteractionMode(EDIT_MODE_PICK);
	}
	
	void setInteractionMode(int mode){
		interactionMode = mode;
		editModeLabel.setText(getEditMode());
		if(interactionMode == EDIT_MODE_PICK){
			game.cursor.setCursorStyle(Cursor.CursorStyle.DEFAULT);
		} else if(interactionMode == EDIT_MODE_CREATE){
			game.cursor.setCursorStyle(Cursor.CursorStyle.CREATE);
		}
	}
	
	void setObjectMode(int mode){
		selectObject(null);
		objectMode = mode;
		createModeLabel.setText(getCreateMode());
	}
	
	Vector4f temp = new Vector4f(0, 0, 0, 1);
	Vector2f getWorldPosition(Vector2f mousePosition){
		Matrix4f cameraMat = new Matrix4f(editorCamera.getViewMatrix());
		cameraMat.invert();
		temp.set(0, 0, 0, 1);
		Vector2f worldPosition = new Vector2f(0, 0);
		
		temp.set(mousePosition.x, mousePosition.y, 0, 1);
		
		cameraMat.transform(temp);
		worldPosition.x = temp.x;
		worldPosition.y = temp.y;
		return worldPosition;
	}
	
	void tryObjectDelete(){
		if(lastSelectedObject != null){
			if(lastSelectedObject instanceof Terrain){
				map.removeTerrain((Terrain) lastSelectedObject);
			} else if(lastSelectedObject instanceof Light){
				map.removeLight((Light) lastSelectedObject);
			} else if(lastSelectedObject instanceof Collider){
				map.removeCollider((Collider) lastSelectedObject);
			} else if(lastSelectedObject instanceof Pickup){
				map.removePickup((Pickup) lastSelectedObject);
			}
			selectObject(null);
		}
	}
	
	void selectObject(Object object){
		lastSelectedObject = object;
		selectedObjectRenderer.select(object);
		if(object != null){
			if(lastSelectedObject instanceof Terrain){
				terrainProperties.get((Terrain) lastSelectedObject);
				terrainOptionsDialog.updateProperties();
			} else if(lastSelectedObject instanceof Light){
				lightProperties.get((Light) lastSelectedObject);
				lightOptionsDialog.updateProperties();
			}
		}
	}
	
	boolean hasSelectedObject(){
		return lastSelectedObject != null;
	}
	
	void interactLeftClick(){
		if(interactionMode == EDIT_MODE_CREATE){
			if(objectMode == CREATE_MODE_TERRAIN){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				if(editorProperties.snapToGrid){
					Vector2f snappedPos = grid.snapPosition(new Vector2f(worldPos));
					worldPos.set(snappedPos);
				}
				map.createTerrainAt(worldPos.x, worldPos.y, terrainProperties);
			} else if(objectMode == CREATE_MODE_LIGHTS){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				if(editorProperties.snapToGrid){
					Vector2f snappedPos = grid.snapPosition(new Vector2f(worldPos));
					worldPos.set(snappedPos);
				}
				map.createLightAt(worldPos.x, worldPos.y, lightProperties);
			} else if(objectMode == CREATE_MODE_SPAWNS){
				
			} else if(objectMode == CREATE_MODE_COLLIDERS){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				if(editorProperties.snapToGrid){
					Vector2f snappedPos = grid.snapPosition(new Vector2f(worldPos));
					worldPos.set(snappedPos);
				}
				map.createColliderAt(worldPos.x, worldPos.y, colliderProperties);
			} else if(objectMode == CREATE_MODE_PICKUPS){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				if(editorProperties.snapToGrid){
					Vector2f snappedPos = grid.snapPosition(new Vector2f(worldPos));
					worldPos.set(snappedPos);
				}
				map.createPickupAt(worldPos.x, worldPos.y, pickupProperties);
			}
		} else if(interactionMode == EDIT_MODE_PICK){
			if(objectMode == CREATE_MODE_TERRAIN){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				Terrain object = map.pickTerrainAt(worldPos.x, worldPos.y);
				if(object != null)
					selectObject(object);
				else 
					selectObject(null);
			} else if(objectMode == CREATE_MODE_LIGHTS){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				Light light = map.pickLightAt(worldPos.x, worldPos.y);
				if(light != null)
					selectObject(light);
				else 
					selectObject(null);
			} else if(objectMode == CREATE_MODE_COLLIDERS){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				Collider collider = map.pickColliderAt(worldPos.x, worldPos.y);
				if(collider != null)
					selectObject(collider);
				else 
					selectObject(null);
			} else if(objectMode == CREATE_MODE_PICKUPS){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				Pickup pickup = map.pickPickupAt(worldPos.x, worldPos.y);
				if(pickup != null)
					selectObject(pickup);
				else 
					selectObject(null);
			}
		}
	}
	
	void interactRightClick(){
		if(interactionMode == EDIT_MODE_CREATE){
			if(objectMode == CREATE_MODE_TERRAIN){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				map.removeTerrainAt(worldPos.x, worldPos.y);
			} else if(objectMode == CREATE_MODE_LIGHTS){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				map.removeLightAt(worldPos.x, worldPos.y);
			} else if(objectMode == CREATE_MODE_COLLIDERS){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				map.removeColliderAt(worldPos.x, worldPos.y);
			} else if(objectMode == CREATE_MODE_PICKUPS){
				Vector2f worldPos = getWorldPosition(game.cursor.getPosition());
				map.removePickupAt(worldPos.x, worldPos.y);
			}
		}
	}
	
	@Override
	public void update() {
		Mouse m = game.input.getMouse();
		mousePos.x = (float)m.getX();
		mousePos.y = (float)m.getY();
		isMMBDown = m.isButtonDown(Mouse.MIDDLE_BUTTON);
		
		grid.setCamera(editorCamera);
		CameraStack stack = game.renderer.getCameraStack();
		stack.current().set(editorCamera);
		
		grid.setDraw(editorProperties.drawGrid);
		grid.setCellSize(editorProperties.cellSize);
		grid.setNumDivisions(editorProperties.numDivisions);
		grid.setCameraOffset(editorCamera);
		
		if(isMMBDown && !mainUIStage.interacting() && isMapOpen())
			game.cursor.setPosition(0, 0);
	}

	@Override
	public void unload() {
		closeMap();
		game.uirenderer.remove(selectedObjectRenderer);
		game.renderer.remove(mapRenderer);
		game.ui.clear();
		game.input.getMouse().removeListener(mouseListener);
		game.input.getKeyboard().removeListener(keyboardListener);
	}

}

class SelectedObjectRenderer extends TypedUIRenderer {

	static final int LAYER = 8500;
	
	final int TYPE_TERRAIN = 1;
	final int TYPE_LIGHT = 2;
	final int TYPE_SPAWN = 3;
	final int TYPE_COLLIDER = 4;
	final int TYPE_PICKUP = 5;
	
	int type = -1;
	
	Object selectedObject = null;
	
	Texture white;
	
	public SelectedObjectRenderer() {
		super(LAYER);
		white = new Texture("images/white.png");
	}

	public void select(Object object){
		if(object instanceof Terrain){
			selectedObject = object;
			type = TYPE_TERRAIN;
		} else if(object instanceof Light){
			selectedObject = object;
			type = TYPE_LIGHT;
		} else if(object instanceof Collider){
			selectedObject = object;
			type = TYPE_COLLIDER;
		} else if(object instanceof Pickup){
			selectedObject = object;
			type = TYPE_PICKUP;
		} else {
			type = -1;
		}
	}
	
	void drawMeshOutline(Transform transform, Mesh mesh, Vector4f color){
		
		Vector2f pi = new Vector2f();
		Vector2f pj = new Vector2f();
		
		for(int i = 0; i < mesh.getNumVertices(); i++){
			int j = (i + 1) % mesh.getNumVertices(); 
			
			Vertex vi = mesh.get(i);
			Vertex vj = mesh.get(j);
			
			pi.set(vi.position());
			pj.set(vj.position());
			
			transform.transform(pi);
			transform.transform(pj);
			
			float dx = pj.x - pi.x;
			float dy = pj.y - pi.y;
			
			float posX = pi.x + (dx / 2.0f);
			float posY = pi.y + (dy / 2.0f);
			
			float h = 4;
			float w = (float)Math.sqrt((dx * dx) + (dy * dy));
			
			float a = (float)Math.atan2(dy, dx);
			
			batch.draw(posX, posY, w, h, a, 1, 1, white, color);
			
		}
	}
	
	void drawCircleOutline(){
		
	}
	
	void drawCircle(float x, float y, float r, Vector4f color){
		
		batch.draw(0, 0, 0, 0, 0, 0, 0, white, Color.WHITE);
		
		for(int i = 0; i < 32; i++){
			float a = (float)i / (32.0f) * 2.0f * (float)Math.PI;
			float a2 = (float)(i + 1.0f) / (32.0f) * 2.0f * (float)Math.PI;
			
			Vector2f center = new Vector2f(x, y);
			Vector2f texcoord = new Vector2f(0, 0);
			Vector2f v0 = new Vector2f();
			Vector2f v1 = new Vector2f();
			
			v0.x = center.x + (float)Math.cos(a) * r;
			v0.y = center.y + (float)Math.sin(a) * r;
			
			v1.x = center.x + (float)Math.cos(a2) * r;
			v1.y = center.y + (float)Math.sin(a2) * r;
			
			batch.draw(center, texcoord, color);
			batch.draw(v1, texcoord, color);
			batch.draw(v0, texcoord, color);
		}
	}
	
	void drawMesh(Transform transform, Mesh mesh, Vector4f color){
		batch.draw(transform, mesh, white, color);
		drawMeshOutline(transform, mesh, color);
	}
	
	void drawSelected(){
		if(selectedObject == null)
			return;
		
		if(type == TYPE_TERRAIN){
		
			Terrain terrain = (Terrain) selectedObject;
			drawMesh(terrain.getTransform(), terrain.getMesh(), new Vector4f(0, 1, 1, 0.09f));
			
		} else if(type == TYPE_LIGHT){
			
			Light light = (Light) selectedObject;
			Vector3f position = light.getPosition();
			float radius = light.getRadius();
			drawCircle(position.x, position.y, radius, new Vector4f(0, 1, 1, 0.09f));
			
		} else if(type == TYPE_COLLIDER){
			Collider collider = (Collider) selectedObject;
			Transform t = collider.getBody().getTransform();
			float x = t.getTranslation().x;
			float y = t.getTranslation().y;
			float w = collider.getSize();
			float h = collider.getSize();
			float r = t.getRotation();
			batch.draw(x, y, w, h, r, 1, 1, white, Color.WHITE_QUARTER_OPAQUE);
		} else if(type == TYPE_PICKUP){
			Pickup pickup = (Pickup) selectedObject;
			Transform t = pickup.getBody().getTransform();
			float x = t.getTranslation().x;
			float y = t.getTranslation().y;
			float w = 64;
			float h = 64;
			float r = t.getRotation();
			batch.draw(x, y, w, h, r, 1, 1, white, Color.WHITE_QUARTER_OPAQUE);
		}
	}
	
	@Override
	public void draw() {
		batch.begin(BlendState.ALPHA_TRANSPARENCY);
			drawSelected();
		batch.end();
	}
	
}

class EditorCamera extends Camera {
	
	static final Vector2f MAX_ZOOM = new Vector2f(2, 2); 
	static final Vector2f MIN_ZOOM = new Vector2f(0.25f, 0.25f);
	Vector2f zoom;
	
	public EditorCamera(int width, int height){
		super(width, height);
		zoom = new Vector2f(1, 1);
	}
	
	public void readProperties(EditorProperties properties){
		zoom.set(properties.cameraZoom);
		setScale(properties.cameraZoom.x);
		setTranslation(properties.cameraPosition.x, properties.cameraPosition.y);
	}
	
	public void move(float dx, float dy){
		translate(dx, dy);
	}
	
	public void zoom(float dScroll){
		zoom.x += dScroll * 0.02f;
		zoom.y += dScroll * 0.02f;
		zoom.x = Math2D.clamp(MIN_ZOOM.x, MAX_ZOOM.x, zoom.x);
		setScale(zoom.x);
	}
	
	public void reset(){
		translation.set(0, 0, 0);
		setScale(1);
		zoom.set(1, 1);
	}
	
}