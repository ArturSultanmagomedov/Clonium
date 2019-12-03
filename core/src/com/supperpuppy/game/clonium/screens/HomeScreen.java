package com.supperpuppy.game.clonium.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.supperpuppy.game.clonium.Game;

public class HomeScreen extends InputAdapter implements Screen {

    //// стандартные поля экрана ////
    private Game game;

    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;

    private SpriteBatch batch;
    /////////////////////////////////

    public HomeScreen(Game game) {

        this.game = game;

        camera = new OrthographicCamera();

        viewport = new ExtendViewport(450 / 3f, 800 / 3f, 600 / 3f, 1000 / 3f, camera);

        createStage();

        batch = new SpriteBatch();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1 / 20f, 1 / 23f, 1 / 25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        ///////////        UI CAMERA        ///////////

        batch.setProjectionMatrix(camera.combined);
        camera.update();

        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
        createStage();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    private void createStage() {

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Label label = new Label("play", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        label.setSize(label.getWidth() * 3, label.getHeight() * 3);
        label.setFontScale(3, 3);
        label.setPosition(stage.getWidth() / 2 - label.getWidth() / 2, stage.getHeight() / 2 - label.getHeight() / 2);
        label.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                game.setScreen(new NewGameScreen(game));
                dispose();

            }
        });

        final Label musicSettings = new Label("A", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        musicSettings.setPosition(stage.getWidth() / 2 - musicSettings.getWidth() / 2, 20);
        musicSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

            }
        });

        stage.addActor(label);
        stage.addActor(musicSettings);

    }

    @Override
    public void dispose() {

        try {
            batch.dispose();
        } catch (Exception ignored) {
        }
        try {
            stage.dispose();
        } catch (Exception ignored) {
        }

    }

}
