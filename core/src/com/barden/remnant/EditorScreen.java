package com.barden.remnant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.barden.remnant.widgets.ConfirmExitDialog;
import com.barden.remnant.widgets.MapTree;
import com.barden.remnant.widgets.MapViewerNode;
import com.barden.remnant.widgets.NewMapDialog;
import com.barden.remnant.widgets.CreateTab;
import com.barden.remnant.widgets.OpenMapDialog;
import com.barden.remnant.widgets.PaletteTab;
import com.barden.remnant.widgets.SaveMapAsDialog;
import com.barden.remnant.widgets.SaveMapDialog;
import com.barden.remnant.widgets.StructureTab;
import com.barden.remnant.widgets.TerrainProperty;
import com.barden.remnant.widgets.TitledPane;
import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuBar;
import com.kotcrab.vis.ui.widget.MenuBar.MenuBarStyle;
import com.kotcrab.vis.ui.widget.MenuItem;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooser.Mode;
import com.kotcrab.vis.ui.widget.file.FileChooser.SelectionMode;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneListener;

public class EditorScreen extends GameScreen {

	Table rootTable;
	
	Lighting lighting;
	GameWorld world;
	GameRenderer worldRenderer;
	
	InputMultiplexer multiplexer;
	EditorInputProcessor editorInput;
	
	FileChooser fileChooser;
	
	NewMapDialog newMapDialog;
	OpenMapDialog openMapDialog;
	SaveMapDialog saveDialog;
	SaveMapAsDialog saveAsDialog;
	ConfirmExitDialog exitDialog;
	
	MapTree tree;
	
	ClickListener viewerListener;
	MapViewerNode mapViewer;
	FrameBuffer mapBuffer;
	OrthographicCamera mapCamera;
	Map map;
	boolean isInteracting = false;
	
	public EditorScreen(Remnant game){
		super(game);
		
		Skin skin = game.skin;
		
		newMapDialog = new NewMapDialog(skin);
		openMapDialog = new OpenMapDialog(skin);
		saveDialog = new SaveMapDialog(skin);
		saveAsDialog = new SaveMapAsDialog(skin);
		exitDialog = new ConfirmExitDialog(skin);
		
		//chooser creation
		fileChooser = new FileChooser(Mode.OPEN);
		fileChooser.setSelectionMode(SelectionMode.DIRECTORIES);
		fileChooser.setListener(new FileChooserAdapter() {
			@Override
			public void selected (Array<FileHandle> file) {
				
			}
		});
		
		rootTable = new Table(skin);
		rootTable.setFillParent(true);
		
		Texture bkd = new Texture(Gdx.files.internal("background.png"));
		TextureRegion region = new TextureRegion(bkd);
		TextureRegionDrawable bkdDrawable = new TextureRegionDrawable(region);
		
		TextureRegion region2 = new TextureRegion(new Texture(Gdx.files.internal("black45.png")));
		TextureRegionDrawable blk45 = new TextureRegionDrawable(region2);
		
		TextureRegion region3 = new TextureRegion(new Texture(Gdx.files.internal("black20.png")));
		TextureRegionDrawable blk20 = new TextureRegionDrawable(region3);
		
		TextureRegion region4 = new TextureRegion(new Texture(Gdx.files.internal("black0.png")));
		TextureRegionDrawable blk0 = new TextureRegionDrawable(region3);
		
		rootTable.setBackground(bkdDrawable);
		
		MenuBar menuBar = new MenuBar(new MenuBarStyle(blk0));
		
		FileMenu fileMenu = new FileMenu(game.stage, fileChooser);
		EditMenu editMenu = new EditMenu();
		ViewMenu viewMenu = new ViewMenu();
		RenderMenu renderMenu = new RenderMenu();
		
		fileMenu.setStyle(blk0);
		editMenu.setStyle(blk0);
		viewMenu.setStyle(blk0);
		renderMenu.setStyle(blk0);
		
		menuBar.addMenu(fileMenu);
		menuBar.addMenu(editMenu);
		menuBar.addMenu(viewMenu);
		menuBar.addMenu(renderMenu);
		
		final Table menu = new Table(skin);
		menu.setColor(1, 1, 1, 1);
		menu.setBackground(blk20);
		
		TextButton closeButton = new TextButton("x", skin);
		closeButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
				super.clicked(event, x, y);
			}
		});
		closeButton.pad(7, 14, 7, 14);
		
		ButtonStyle style = closeButton.getStyle();
		style.up = blk20;
		style.down = blk45;
		style.over = blk20;
		style.checked = blk20;
		
		HorizontalGroup menuGroup = new HorizontalGroup();
		menuGroup.addActor(menuBar.getTable());
		menu.add(menuGroup).left().colspan(5).expand().fill();
		menu.add(closeButton).right().colspan(1).expandY().fillY();
		
		Table content = new Table(skin);
		content.setColor(1, 1, 1, 1);
		content.setBackground(blk20);
		
		final TitledPane treeView = new TitledPane("Map", skin);
		TitledPane mapView = new TitledPane("Render", skin);
		TitledPane propertiesView = new TitledPane("Properties", skin);
		
		treeView.titleLabel.setBackground(blk20);
		mapView.titleLabel.setBackground(blk20);
		propertiesView.titleLabel.setBackground(blk20);
		
		//treeView.content.debug();
		//treeView.debug();
		//content.debug();
		
		StructureTab stab = new StructureTab(skin);
		CreateTab otab = new CreateTab(skin);
		
		TabbedPane tabPane = new TabbedPane();
		tabPane.add(stab);
		tabPane.add(otab);
		
		final Table tabArea = new Table(skin);
		
		treeView.content.add(tabPane.getTable()).expandX().fillX().top().row();
		treeView.content.add(tabArea).expand().fill().top();
		treeView.content.align(Align.top);
		
		tabPane.addListener(new TabbedPaneListener() {
			@Override
			public void switchedTab(Tab tab) {
				Table content = tab.getContentTable();
				tabArea.clearChildren();
				tabArea.add(content).expand().fill();
			}

			@Override
			public void removedAllTabs() {
				
			}

			@Override
			public void removedTab(Tab arg0) {
				
			}
		});
		
		tree = new MapTree(skin);
		stab.getContentTable().add(tree).expand().fill();
		
		viewerListener = new ClickListener();
		mapViewer = new MapViewerNode(skin);
		mapViewer.addListener(viewerListener);
		
		mapView.content.add(mapViewer).top().fill().expand().grow();
		mapView.pack();
		
		TerrainProperty tp = new TerrainProperty(skin);
		propertiesView.content.add(tp).expand().fill().top();
		
		content.add(treeView).right().fill().expand().colspan(2);
		content.add(mapView).center().fill().expand().colspan(4);
		content.add(propertiesView).left().top().fill().expand().colspan(2);
		content.setBackground(blk20);
		content.top();
		content.pack();
		
		Table propertiesBar = new Table(skin);
		propertiesBar.setBackground(blk20);
		
		tp.setVisible(true);
		tree.setVisible(true);
		
		rootTable.add(menu).fillX().expandX().row();
		rootTable.add(content).fill().expand().row();
		rootTable.add(propertiesBar).fillX().expandX();
		rootTable.pack();
		
		tabPane.switchTab(0);
		
		lighting = new Lighting();
		
		world = new GameWorld(game);
		worldRenderer = new GameRenderer(game.batch);
		
		float viewerWidth = mapViewer.getWidth();
		float viewerHeight = mapViewer.getHeight();
		
		mapBuffer = new FrameBuffer(Format.RGBA8888, (int)viewerWidth, (int)viewerHeight, false);
		mapCamera = new OrthographicCamera((int)viewerWidth, (int)viewerHeight);
		map = new Map(lighting, world);
		
		editorInput = new EditorInputProcessor();
		
		multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(game.stage);
		multiplexer.addProcessor(editorInput);
	}

	@Override
	public void show() {
		game.stage.addActor(rootTable);
		Gdx.input.setInputProcessor(multiplexer);
		map.load();
		tree.loadMap(map);
	}

	@Override
	public void hide() {
		rootTable.remove();
		map.unload();
		tree.clear();
	}
	
	@Override
	public void update() {
		
		if(viewerListener.isOver()){
			mapCamera.zoom += ((float)editorInput.getScroll() * 0.1f);
			editorInput.scroll = 0;
		}
		
		if(viewerListener.isOver() && editorInput.middleMBDown) {
			mapCamera.translate(-Gdx.input.getDeltaX(), -Gdx.input.getDeltaY());
		}
		
		mapCamera.update();
	}

	@Override
	public void render(float delta) {
		mapBuffer.begin();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			worldRenderer.setCamera(mapCamera);
			worldRenderer.draw(lighting, world);
		mapBuffer.end(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		mapViewer.setTexture(mapBuffer.getColorBufferTexture());
	}
}

class FileMenu extends Menu {
	
	Stage stage;
	FileChooser fileChooser;
	
	MenuItem newMap;
	MenuItem openMap;
	MenuItem saveAsMap;
	MenuItem saveMap;
	MenuItem closeMap;
	MenuItem exit;
	
	public FileMenu(final Stage stage, final FileChooser fileChooser){
		super("File");
		
		super.openButton.pad(10);
	
		this.stage = stage;
		this.fileChooser = fileChooser;
		
		newMap = new MenuItem("New Map");
		newMap.setShortcut(Keys.CONTROL_LEFT, Keys.N);
		
		openMap = new MenuItem("Open Map");
		openMap.setShortcut(Keys.CONTROL_LEFT, Keys.O);
		openMap.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				stage.addActor(fileChooser.fadeIn());
				super.clicked(event, x, y);
			}
		});
		
		saveAsMap = new MenuItem("Save Map As");
		saveAsMap.setShortcut(Keys.CONTROL_LEFT, Keys.SHIFT_LEFT, Keys.S);
		
		saveMap = new MenuItem("Save Map");
		saveMap.setShortcut(Keys.CONTROL_LEFT, Keys.S);
		
		closeMap = new MenuItem("Close Map");
		closeMap.setShortcut(Keys.CONTROL_LEFT, Keys.W);
		
		exit = new MenuItem("Exit");
		exit.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
				super.clicked(event, x, y);
			}
		});
		
		addItem(newMap);
		addItem(openMap);
		addItem(saveAsMap);
		addItem(saveMap);
		addItem(closeMap);
		addSeparator();
		addItem(exit);
	}
	
	public void setStyle(com.badlogic.gdx.scenes.scene2d.utils.Drawable d){
		openButton.getStyle().up = d;
		buttonDefault = d;
	}
	
	public void setItemBackground(com.badlogic.gdx.scenes.scene2d.utils.Drawable d){
		//newMap.setBackground(d);
		//openMap.setBackground(d);
	}
	
}

class EditMenu extends Menu {
	
	MenuItem undoItem;
	MenuItem redoItem;
	
	public EditMenu(){
		super("Edit");
		
		super.openButton.pad(10);
		
		undoItem = new MenuItem("Undo");
		undoItem.setShortcut(Keys.CONTROL_LEFT, Keys.Z);
		
		redoItem = new MenuItem("Redo");
		redoItem.setShortcut(Keys.CONTROL_LEFT, Keys.Y);

		undoItem.setDisabled(true);
		redoItem.setDisabled(true);
		
		addItem(undoItem);
		addItem(redoItem);
		
	}
	
	public void setStyle(com.badlogic.gdx.scenes.scene2d.utils.Drawable d){
		openButton.getStyle().up = d;
		buttonDefault = d;
	}
	
}

class ViewMenu extends Menu {
	
	MenuItem resetCameraItem;
	MenuItem centerOnSelectedItem;
	MenuItem gridSettingsItem;
	
	public ViewMenu(){
		super("View");
		
		super.openButton.pad(10);
		
		resetCameraItem = new MenuItem("Reset Camera To Default");
		centerOnSelectedItem = new MenuItem("Center On Selected");
		gridSettingsItem = new MenuItem("Grid Settings");
		
		resetCameraItem.setDisabled(true);
		centerOnSelectedItem.setDisabled(true);
		gridSettingsItem.setDisabled(true);
		
		addItem(resetCameraItem);
		addItem(centerOnSelectedItem);
		addSeparator();
		addItem(gridSettingsItem);
	}
	
	public void setStyle(com.badlogic.gdx.scenes.scene2d.utils.Drawable d){
		openButton.getStyle().up = d;
		buttonDefault = d;
	}
	
}

class RenderMenu extends Menu {
	
	MenuItem mapItem;
	MenuItem lightingItem;
	MenuItem postProcessingItem;
	
	public RenderMenu(){
		super("Render Settings");
		
		super.openButton.pad(10);
		
		mapItem = new MenuItem("Map");
		lightingItem = new MenuItem("Lighting");
		postProcessingItem = new MenuItem("Post Processing");
		
		mapItem.setDisabled(true);
		lightingItem.setDisabled(true);
		postProcessingItem.setDisabled(true);
		
		addItem(mapItem);
		addItem(lightingItem);
		addItem(postProcessingItem);
		
	}
	
	public void setStyle(com.badlogic.gdx.scenes.scene2d.utils.Drawable d){
		openButton.getStyle().up = d;
		buttonDefault = d;
	}
	
}