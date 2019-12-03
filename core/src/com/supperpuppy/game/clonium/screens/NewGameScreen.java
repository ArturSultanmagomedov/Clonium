package com.supperpuppy.game.clonium.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.supperpuppy.game.clonium.Game;

public class NewGameScreen extends InputAdapter implements Screen {

    private Game game;

    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;
    private Label label;

    private SpriteBatch batch;

    private Initializer i1, i2, i3, i4;

    NewGameScreen(Game game) {

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

        {
            int count = 0;

            if (!i1.getPlayerMode().equals("null")) count++;
            if (!i2.getPlayerMode().equals("null")) count++;
            if (!i3.getPlayerMode().equals("null")) count++;
            if (!i4.getPlayerMode().equals("null")) count++;

            int c = 0;

            if (i1.getPlayerMode().equals("player")) c++;
            if (i2.getPlayerMode().equals("player")) c++;
            if (i3.getPlayerMode().equals("player")) c++;
            if (i4.getPlayerMode().equals("player")) c++;

            if (count > 1 && c > 0) label.setColor(Color.WHITE);
            else label.setColor(Color.DARK_GRAY);
        }

    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);

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

        Table table = new Table();
        table.setSize(100, 100);
        table.setPosition(stage.getWidth() / 2 - 50, stage.getHeight() / 2 - 50);

        {
            i1 = new Initializer(1);
            i1.setPosition(5, 5);
            table.addActor(i1);

            i2 = new Initializer(2);
            i2.setPosition(65, 5);
            table.addActor(i2);

            i3 = new Initializer(3);
            i3.setPosition(5, 65);
            table.addActor(i3);

            i4 = new Initializer(4);
            i4.setPosition(65, 65);
            table.addActor(i4);
        }

        label = new Label("play", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        label.setSize(label.getWidth() * 1.8f, label.getHeight() * 1.8f);
        label.setFontScale(1.8f, 1.8f);
        label.setPosition(stage.getWidth() / 2 - label.getWidth() / 2, table.getY() - label.getHeight() - 10);
        label.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                int count = 0;

                if (!i1.getPlayerMode().equals("null")) count++;
                if (!i2.getPlayerMode().equals("null")) count++;
                if (!i3.getPlayerMode().equals("null")) count++;
                if (!i4.getPlayerMode().equals("null")) count++;

                int c = 0;

                if (i1.getPlayerMode().equals("player")) c++;
                if (i2.getPlayerMode().equals("player")) c++;
                if (i3.getPlayerMode().equals("player")) c++;
                if (i4.getPlayerMode().equals("player")) c++;

                if (count > 1 && c > 0) {

                    PlayScreen playScreen = new PlayScreen(game);

                    playScreen.initPlayers("bot", i1.getPlayerMode().equals("bot"), i2.getPlayerMode().equals("bot"),
                            i3.getPlayerMode().equals("bot"), i4.getPlayerMode().equals("bot"));
                    playScreen.initPlayers("player", i1.getPlayerMode().equals("player"), i2.getPlayerMode().equals("player"),
                            i3.getPlayerMode().equals("player"), i4.getPlayerMode().equals("player"));

                    game.setScreen(playScreen);
                    dispose();

                }

            }
        });

        stage.addActor(table);
        stage.addActor(label);

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

final class Initializer extends Table {

    private final String NULL = "null", PLAYER = "player", BOT = "bot";

    private String playerMode = NULL;

    Initializer(int type) {

        final Image modeImg, initImg;

        {
            initImg = new Image();

            initImg.setDrawable(new TextureRegionDrawable(new TextureRegion(
                    new Texture("images/img.png"), 33, 0, 14, 14)));
            initImg.setColor(
                    type == 1 ? Color.ROYAL :
                            type == 2 ? Color.MAROON :
                                    type == 3 ? Color.PURPLE :
                                            type == 4 ? Color.TEAL :
                                                    Color.CLEAR);
            initImg.setSize(30, 30);
            initImg.setPosition(0, 0);
            this.addActor(initImg);
        }
        {
            modeImg = new Image();

            modeImg.setDrawable(new TextureRegionDrawable(new TextureRegion(
                    new Texture("images/img.png"), 33 + 14 + 18, 0, 14, 14)));
            modeImg.setColor(Color.WHITE);
            modeImg.setSize(24, 24);
            modeImg.setPosition(3, 3);
            modeImg.setVisible(!playerMode.equals(NULL));
            this.addActor(modeImg);
        }

        setPosition(0, 0);
        setSize(30, 30);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                playerMode = playerMode.equals(NULL) ? PLAYER : playerMode.equals(PLAYER) ? BOT : NULL;

                modeImg.setVisible(!playerMode.equals(NULL));

                if (playerMode.equals(PLAYER))
                modeImg.setDrawable(new TextureRegionDrawable(new TextureRegion(
                        new Texture("images/img.png"), 9, 91, 9, 9)));

                if (playerMode.equals(BOT))
                modeImg.setDrawable(new TextureRegionDrawable(new TextureRegion(
                        new Texture("images/img.png"), 0, 91, 9, 9)));

                super.clicked(event, x, y);

            }
        });

    }

    String getPlayerMode() {
        return playerMode;
    }

}
