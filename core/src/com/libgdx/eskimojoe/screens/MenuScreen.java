package com.libgdx.eskimojoe.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.libgdx.eskimojoe.game.Assets;
import com.libgdx.eskimojoe.util.Constants;

public class MenuScreen extends AbstractScreen 
{
	private static final String TAG = MenuScreen.class.getName();
	
	private Stage stage;
	private Skin skinEskimoJoe;
	
	// menu
	private Image imgBackground;
	private Image imgLogo;
	private Image imgInfo;
	private Image imgFish;
	private Image imgJoe;
	private Button btnMenuPlay;
	private Button btnMenuOptions;
	
	// options
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkMusic;
	private Slider sldMusic;
	private Image imgCharSkin;
	private CheckBox chkShowFpsCounter;
	
	// debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;
	
	
	public MenuScreen (Game game) 
	{
		super(game);
	}
	
	private void rebuildStage () 
	{
		skinEskimoJoe = new Skin(
		Gdx.files.internal(Constants.SKIN_ESKIMOJOE_UI), new TextureAtlas(
				Constants.TEXTURE_ATLAS_UI));
		
		// build all layers
		Table layerBackground = buildBackgroundLayer();
		Table layerObjects = buildObjectsLayer();
		Table layerLogos = buildLogosLayer();
		Table layerControls = buildControlsLayer();
		Table layerOptionsWindow = buildOptionsWindowLayer();
		
		// assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerObjects);
		stack.add(layerLogos);
		stack.add(layerControls);
		stage.addActor(layerOptionsWindow);
	}
	
	private Table buildBackgroundLayer () 
	{
		Table layer = new Table();
		// + Background
		imgBackground = new Image(skinEskimoJoe, "background");
		layer.add(imgBackground);
		return layer;
	}
	
	private Table buildObjectsLayer () 
	{
		Table layer = new Table();
		// + Coins
		imgFish = new Image(skinEskimoJoe, "fish");
		layer.addActor(imgFish);
		imgFish.setPosition(135, 80);
		// + Bunny
		imgJoe = new Image(skinEskimoJoe, "eskimojoe");
		layer.addActor(imgJoe);
		imgJoe.setPosition(355, 40);
		return layer;
	}
	
	private Table buildLogosLayer () 
	{
		Table layer = new Table();
		layer.left().top();
		// + Game Logo
		imgLogo = new Image(skinEskimoJoe, "logo");
		layer.add(imgLogo);
		layer.row().expandY();
		// + Info Logos
		imgInfo = new Image(skinEskimoJoe, "info");
		layer.add(imgInfo).bottom();
		if (debugEnabled) layer.debug();
		return layer;
	}
	
	private Table buildControlsLayer () 
	{
		Table layer = new Table();
		layer.right().bottom();
		// + Play Button
		btnMenuPlay = new Button(skinEskimoJoe, "play");
		layer.add(btnMenuPlay);
		btnMenuPlay.addListener(new ChangeListener() {
		@Override
		public void changed (ChangeEvent event, Actor actor) {
		onPlayClicked();
		}
		});
		layer.row();
		// + Options Button
		btnMenuOptions = new Button(skinEskimoJoe, "options");
		layer.add(btnMenuOptions);
		btnMenuOptions.addListener(new ChangeListener() {
		@Override
		public void changed (ChangeEvent event, Actor actor) {
		onOptionsClicked();
		}
		});
		if (debugEnabled) layer.debug();
		return layer;
	}
	
	private Table buildOptionsWindowLayer () 
	{
		Table layer = new Table();
		return layer;
	}
	
	private void onPlayClicked () 
	{
		game.setScreen(new GameScreen(game));
	}
	
	private void onOptionsClicked () 
	{
		
	}
	
	@Override
	public void render (float deltaTime) 
	{
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		if(Gdx.input.isTouched())
		{
			game.setScreen(new GameScreen(game));
		}
		
		if (debugEnabled) 
		{
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0) 
			{
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
			stage.act(deltaTime);
			stage.draw();
			//Table.drawDebug(stage);
	}
	
	@Override public void resize (int width, int height) 
	{ 
		stage.getViewport().update(width, height, true);
	}
	
	@Override public void show () 
	{
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
				Constants.VIEWPORT_GUI_HEIGHT));
		Gdx.input.setInputProcessor(stage);
		rebuildStage();
	}
	
	@Override public void hide () 
	{
		stage.dispose();
		skinEskimoJoe.dispose();
	}
	
	@Override public void pause () { }
}