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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.supperpuppy.game.clonium.Game;

public class PlayScreen extends InputAdapter implements Screen {

    //// стандартные поля экрана ////
    private Game game;

    private OrthographicCamera camera;
    private Stage stage;
    private Viewport viewport;

    private SpriteBatch batch;
    /////////////////////////////////

    private Label theEndLabel;

    Chip[][] board = new Chip[8][8];
    ChipRenderer[][] boardRenderer = new ChipRenderer[8][8];
    private Image[][] boardCells = new Image[8][8];

    Queue<QueueParameter> animQueue = new Queue<QueueParameter>();

    Drawable cloneDr, oneDotDr, twoDotsDr, threeDotsDr,
            fourDotsDr, fiveDotsDr, st1, st2, st3, st4, st5, st6;
    private Drawable cellDr;

    private Color cellColor = new Color(60 / 255f, 63 / 255f, 65 / 255f, 1),
            targetCellColor = new Color(0.6f, 0.55f, 0.3f, 1f);

    // цвет текущего игрока
    int type;
    // объявление ботов
    private Bot royalBot = new Bot(1, this), maroonBot = new Bot(2, this),
            purpleBot = new Bot(3, this), tealBot = new Bot(4, this);

    private Counter c1, c2, c3, c4;

    // для инициализации игроков
    private boolean pl1, pl2, pl3, pl4;

    // факт окончания игры
    private boolean theEnd = false;

    PlayScreen(Game game) {

        this.game = game;

        camera = new OrthographicCamera();

        viewport = new ExtendViewport(450 / 3f, 800 / 3f, 600 / 3f, 1000 / 3f, camera);

        // определение первого хода
        type = (int) (Math.random() * 4) + 1;

        batch = new SpriteBatch();

        init();

        createUI();

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(20 / 255f, 23 / 255f, 25 / 255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        ///////////        UI CAMERA        ///////////

        batch.setProjectionMatrix(camera.combined);
        camera.update();


        royalBot.update();
        maroonBot.update();
        purpleBot.update();
        tealBot.update();

        c1.update();
        c2.update();
        c3.update();
        c4.update();


        if (theEnd) {
            theEndLabel.setPosition(stage.getWidth() / 2 - theEndLabel.getWidth() / 2,
                    stage.getHeight() / 2 - theEndLabel.getHeight() / 2);
            theEndLabel.setVisible(true);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    boardCells[i][j].setColor(cellColor);
                }
            }
        } else {
            int k = 0, k2 = 0, p = 0, q = 0;
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j].type != type && board[i][j].type != 0) k++;
                    if (!boardRenderer[i][j].animate.isEnd()) k2++;

                    if (board[i][j].type == type) {
                        p++;
                        boardCells[i][j].setColor(targetCellColor);
                    } else {
                        boardCells[i][j].setColor(cellColor);
                    }

                    if (!boardRenderer[i][j].animate.isEnd()) q++;
                }
            }

            if (k2 == 0 && animQueue.isEmpty()) {
                if (k == 0) theEnd = true;

                if (p == 0 && q == 0) next();
            }
        }


        if (animQueue.notEmpty()) {
            QueueParameter f = animQueue.first();
            if (boardRenderer[f.x][f.y].animate.isEnd()) {
                if (f.x > 0) {
                    board[f.x - 1][f.y].num++;
                    board[f.x - 1][f.y].type = f.type;
                    update(f.x - 1, f.y);
                }
                if (f.y > 0) {
                    board[f.x][f.y - 1].num++;
                    board[f.x][f.y - 1].type = f.type;
                    update(f.x, f.y - 1);
                }
                if (f.x < 7) {
                    board[f.x + 1][f.y].num++;
                    board[f.x + 1][f.y].type = f.type;
                    update(f.x + 1, f.y);
                }
                if (f.y < 7) {
                    board[f.x][f.y + 1].num++;
                    board[f.x][f.y + 1].type = f.type;
                    update(f.x, f.y + 1);
                }
                animQueue.removeFirst();
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardRenderer[i][j].animate.update();
            }
        }


        stage.act(delta);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

        viewport.update(width, height);
        createUI();

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

    private void init() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Chip();
            }
        }

        initDrawables();

    }

    void pop(int x, int y) {
        if (board[x][y].num > 0) {
            board[x][y].num++;
        }
        update(x, y);
    }

    private void update(int x, int y) {
        boardRenderer[x][y].update();

        if (board[x][y].num > 3) {
            int type = board[x][y].type;
            board[x][y].num -= 4;

            boardRenderer[x][y].animate.start();

            animQueue.addLast(new QueueParameter(x, y, type));

        }
        boardRenderer[x][y].update();

        if (board[x][y].num == 0) {
            board[x][y].type = 0;
        }
        boardRenderer[x][y].update();
    }

    // переход хода
    void next() {

        if (type == 1) type = 2;
        else if (type == 2) type = 3;
        else if (type == 3) type = 4;
        else if (type == 4) type = 1;

    }

    private void initDrawables() {

        Texture t1 = new Texture("images/img.png");
        cloneDr = new TextureRegionDrawable(new TextureRegion(t1, 33, 0, 14, 14));
        cellDr = new TextureRegionDrawable(new TextureRegion(t1, 0, 0, 33, 33));
        oneDotDr = new TextureRegionDrawable(new TextureRegion(t1, 33 + 14 + 18, 0, 14, 14));
        twoDotsDr = new TextureRegionDrawable(new TextureRegion(t1, 33 + 14 + 18 + 14, 0, 14, 14));
        threeDotsDr = new TextureRegionDrawable(new TextureRegion(t1, 33 + 14 + 18, 14, 14, 14));
        fourDotsDr = new TextureRegionDrawable(new TextureRegion(t1, 33 + 14 + 18 + 14, 14, 14, 14));
        fiveDotsDr = new TextureRegionDrawable(new TextureRegion(t1, 33, 14, 14, 14));

        Texture t2 = new Texture("images/chip_anim.png");
        st1 = new TextureRegionDrawable(new TextureRegion(t2, 0, 0, 30, 30));
        st2 = new TextureRegionDrawable(new TextureRegion(t2, 30, 0, 30, 30));
        st3 = new TextureRegionDrawable(new TextureRegion(t2, 60, 0, 30, 30));
        st4 = new TextureRegionDrawable(new TextureRegion(t2, 90, 0, 30, 30));
        st5 = new TextureRegionDrawable(new TextureRegion(t2, 120, 0, 30, 30));
        st6 = new TextureRegionDrawable(new TextureRegion(t2, 150, 0, 30, 30));

    }

    private void createUI() {

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        createBoard();

        {
            Label label1 = new Label("restart", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
            label1.setPosition(10, 10);
            label1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    game.setScreen(new NewGameScreen(game));
                    dispose();

                }
            });
            stage.addActor(label1);
        }

        {
            Label label2 = new Label("home", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
            label2.setPosition(stage.getWidth() - 5 - label2.getWidth(), 10);
            label2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);

                    game.setScreen(new HomeScreen(game));
                    dispose();

                }
            });
            stage.addActor(label2);
        }

        {
            theEndLabel = new Label("The End?", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
            theEndLabel.setFontScale(1.7f);
            theEndLabel.setSize(theEndLabel.getWidth() * 1.7f, theEndLabel.getHeight() * 1.7f);
            theEndLabel.setPosition(stage.getWidth() / 2 - theEndLabel.getWidth() / 2,
                    stage.getHeight() / 2 - theEndLabel.getHeight() / 2);
            theEndLabel.setVisible(false);
        }

        stage.addActor(theEndLabel);

    }

    private void createBoard() {

        Table boardTable = new Table();
        boardTable.setSize(18 * 8, 18 * 8);
        boardTable.setPosition(stage.getWidth() / 2 - boardTable.getWidth() / 2,
                stage.getHeight() / 2 - boardTable.getHeight() / 2);

        {
            c1 = new Counter(1, this);
            c2 = new Counter(2, this);
            c3 = new Counter(3, this);
            c4 = new Counter(4, this);

            c1.setPosition(boardTable.getX() + 2, boardTable.getY() - c1.getHeight() - 2);
            c3.setPosition(boardTable.getX() + 2, boardTable.getY() + boardTable.getHeight() + 2);
            c2.setPosition(boardTable.getWidth() - c3.getWidth() - 2,
                    boardTable.getY() - c3.getHeight() - 2);
            c4.setPosition(boardTable.getWidth() - c4.getWidth() - 2,
                    boardTable.getY() + boardTable.getHeight() + 2);

            stage.addActor(c1);
            stage.addActor(c2);
            stage.addActor(c3);
            stage.addActor(c4);
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Image cellImg = new Image();
                cellImg.setDrawable(cellDr);
                cellImg.setColor(cellColor);
                cellImg.setSize(18, 18);
                cellImg.setPosition(18 * i, 18 * j);
                boardCells[i][j] = cellImg;
                boardTable.addActor(cellImg);
            }
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardRenderer[i][j] = new ChipRenderer(i, j, this);
                boardTable.addActor(boardRenderer[i][j]);
            }
        }

        stage.addActor(boardTable);

    }

    void initPlayers(String type, boolean b1, boolean b2, boolean b3, boolean b4) {

        if (!type.equals("null")) {
            if (!pl1) pl1 = b1;
            if (!pl2) pl2 = b2;
            if (!pl3) pl3 = b3;
            if (!pl4) pl4 = b4;
        }
        if (type.equals("bot")) {
            if (b1) royalBot.init();
            if (b2) maroonBot.init();
            if (b3) purpleBot.init();
            if (b4) tealBot.init();
        }

        if (type.equals("player"))
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {

                    Chip chip = board[i][j];
                    int cellId = j * 8 + i + 1;

                    if (cellId == 10 && pl1) {
                        chip.num = 3;
                        chip.type = 1;
                    } else if (cellId == 15 && pl2) {
                        chip.num = 3;
                        chip.type = 2;
                    } else if (cellId == 50 && pl3) {
                        chip.num = 3;
                        chip.type = 3;
                    } else if (cellId == 55 && pl4) {
                        chip.num = 3;
                        chip.type = 4;
                    }
                }
            }

    }

    @Override
    public void dispose() {

        royalBot.stop();
        tealBot.stop();
        purpleBot.stop();
        maroonBot.stop();

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

class QueueParameter {

    int x, y, type;

    QueueParameter(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

}

class Chip {

    int num = 0, type = 0;

    Chip() {

    }

}

class ChipRenderer extends Table {

    private final int x, y;
    private PlayScreen s;

    private Image cloneImg, dotsImg;
    AnimatedChip animate;

    ChipRenderer(int x, int y, final PlayScreen s) {
        this.x = x;
        this.y = y;
        this.s = s;

        setSize(18, 18);
        setPosition(18 * x, 18 * y);

        {
            animate = new AnimatedChip(s.st1, s.st2, s.st3, s.st4, s.st5, s.st6);
            addActor(animate);
        }

        {
            cloneImg = new Image();
            cloneImg.setDrawable(s.cloneDr);
            cloneImg.setColor(
                    s.board[x][y].type == 1 ? Color.ROYAL :
                            s.board[x][y].type == 2 ? Color.MAROON :
                                    s.board[x][y].type == 3 ? Color.PURPLE :
                                            s.board[x][y].type == 4 ? Color.TEAL :
                                                    Color.CLEAR);
            cloneImg.setSize(14, 14);
            cloneImg.setPosition(2, 2);
            cloneImg.setVisible(s.board[x][y].num > 0);
            addActor(cloneImg);
        }

        {
            dotsImg = new Image();

            dotsImg.setDrawable(s.board[x][y].num == 1 ? s.oneDotDr :
                    s.board[x][y].num == 2 ? s.twoDotsDr :
                            s.board[x][y].num == 3 ? s.threeDotsDr :
                                    s.board[x][y].num == 4 ? s.fourDotsDr : s.fiveDotsDr);
            dotsImg.setColor(Color.WHITE);
            dotsImg.setSize(14, 14);
            dotsImg.setPosition(2, 2);
            dotsImg.setVisible(s.board[x][y].num > 0);
            addActor(dotsImg);
        }

        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                if (s.animQueue.isEmpty())
                    click();
            }
        });
    }

    void click() {
        if (s.board[x][y].type == s.type) {
            s.pop(x, y);
            s.next();
        }
    }

    void update() {
        cloneImg.setVisible(s.board[x][y].num > 0);
        dotsImg.setVisible(s.board[x][y].num > 0);

        cloneImg.setColor(
                s.board[x][y].type == 1 ? Color.ROYAL :
                        s.board[x][y].type == 2 ? Color.MAROON :
                                s.board[x][y].type == 3 ? Color.PURPLE :
                                        s.board[x][y].type == 4 ? Color.TEAL :
                                                Color.CLEAR);
        if (s.animQueue.notEmpty()) {
            int f = s.animQueue.first().type;
            animate.setColor(
                    f == 1 ? Color.ROYAL :
                            f == 2 ? Color.MAROON :
                                    f == 3 ? Color.PURPLE :
                                            f == 4 ? Color.TEAL :
                                                    Color.CLEAR);
        }

        dotsImg.setDrawable(s.board[x][y].num == 1 ? s.oneDotDr :
                s.board[x][y].num == 2 ? s.twoDotsDr :
                        s.board[x][y].num == 3 ? s.threeDotsDr :
                                s.board[x][y].num == 4 ? s.fourDotsDr : s.fiveDotsDr);
    }

}

class AnimatedChip extends Image {

    private final Drawable st1, st2, st3, st4, st5, st6;

    private boolean end = true;

    private int step = 0;
    private long lastTime;

    AnimatedChip(Drawable st1, Drawable st2, Drawable st3,
                 Drawable st4, Drawable st5, Drawable st6) {
        this.st1 = st1;
        this.st2 = st2;
        this.st3 = st3;
        this.st4 = st4;
        this.st5 = st5;
        this.st6 = st6;

        setSize(42, 42);
        setPosition(-12, -12);
        setVisible(false);

    }

    void start() {

        end = false;
        setVisible(true);

        step = 1;
        setDrawable(st1);
        lastTime = TimeUtils.nanoTime();

    }

    private void stop() {

        end = true;
        setVisible(false);

        step = 0;
        lastTime = 0;

    }

    boolean isEnd() {
        return end;
    }

    void update() {

        if (!end)
            if (TimeUtils.nanoTime() - lastTime > 1000 * 1000 * (1000 * 0.09f) && step < 8) {

                step++;

                lastTime = TimeUtils.nanoTime();

                switch (step) {
                    case 1:
                        setDrawable(st1);
                        break;
                    case 2:
                        setDrawable(st2);
                        break;
                    case 3:
                        setDrawable(st3);
                        break;
                    case 4:
                        setDrawable(st4);
                        break;
                    case 5:
                        setDrawable(st5);
                        break;
                    case 6:
                        setDrawable(st6);
                        break;
                    case 7:
                        stop();
                        break;
                }

            }

    }

}

class Counter extends Table {

    private int type;
    private PlayScreen s;

    private Label countLabel;

    Counter(int type, PlayScreen s) {
        this.type = type;
        this.s = s;

        setSize(27, 27);

        {
            Image baseImg = new Image();
            baseImg.setDrawable(s.cloneDr);
            baseImg.setColor(
                    type == 1 ? Color.ROYAL :
                            type == 2 ? Color.MAROON :
                                    type == 3 ? Color.PURPLE :
                                            type == 4 ? Color.TEAL :
                                                    Color.CLEAR);
            baseImg.setSize(getWidth(), getHeight());
            addActor(baseImg);
        }

        {
            countLabel = new Label("   ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
            countLabel.setSize(countLabel.getWidth() * 0.5f, countLabel.getHeight() * 0.5f);
            countLabel.setFontScale(0.5f, 0.5f);
            countLabel.setPosition(getWidth() / 2 - countLabel.getWidth() / 2,
                    getHeight() / 2 - countLabel.getHeight() / 2);
            addActor(countLabel);
        }

    }

    void update() {

        int chipCount = 0, dotsCount = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (s.board[i][j].type == type) {
                    chipCount++;
                    dotsCount += s.board[i][j].num;
                }
            }
        }

        countLabel.setText(chipCount + "\n" + dotsCount);

        countLabel.setPosition(getWidth() / 2 - countLabel.getWidth() / 2,
                getHeight() / 2 - countLabel.getHeight() / 2);

    }

}

class Bot {

    private int type;
    private PlayScreen s;

    // приоритеты хода
    private Array<ChipRenderer> chips, p1, p2, p3, p4, p5, p6;

    private boolean act = false;

    Bot(int type, PlayScreen s) {
        this.type = type;
        this.s = s;

        chips = new Array<ChipRenderer>();
        p1 = new Array<ChipRenderer>();
        p2 = new Array<ChipRenderer>();
        p3 = new Array<ChipRenderer>();
        p4 = new Array<ChipRenderer>();
        p5 = new Array<ChipRenderer>();
        p6 = new Array<ChipRenderer>();
    }

    // запуск бота
    void init() {
        act = true;
    }

    // остановка бота
    void stop() {
        act = false;
    }

    void update() {

        if (act) {

            if (s.animQueue.isEmpty())
                aiClick();

        }

    }

    private void aiClick() {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (s.board[i][j].type == type) {
                    chips.add(s.boardRenderer[i][j]);

                    if (s.board[i][j].num == 3) {
                        if ((i > 0 && s.board[i - 1][j].type != type && s.board[i - 1][j].num == 3) ||
                                (j > 0 && s.board[i][j - 1].type != type && s.board[i][j - 1].num == 3) ||
                                (i < 7 && s.board[i + 1][j].type != type && s.board[i + 1][j].num == 3) ||
                                (j < 7 && s.board[i][j + 1].type != type && s.board[i][j + 1].num == 3)) {
                            p1.add(s.boardRenderer[i][j]);
                        }
                    }
                    if (s.board[i][j].num == 2) {
                        if ((i > 0 && s.board[i - 1][j].type != type && s.board[i - 1][j].num == 2) ||
                                (j > 0 && s.board[i][j - 1].type != type && s.board[i][j - 1].num == 2) ||
                                (i < 7 && s.board[i + 1][j].type != type && s.board[i + 1][j].num == 2) ||
                                (j < 7 && s.board[i][j + 1].type != type && s.board[i][j + 1].num == 2)) {
                            p2.add(s.boardRenderer[i][j]);
                        }
                    }
                    if (s.board[i][j].num == 1) {
                        if ((i > 0 && s.board[i - 1][j].type != type && s.board[i - 1][j].num == 1) ||
                                (j > 0 && s.board[i][j - 1].type != type && s.board[i][j - 1].num == 1) ||
                                (i < 7 && s.board[i + 1][j].type != type && s.board[i + 1][j].num == 1) ||
                                (j < 7 && s.board[i][j + 1].type != type && s.board[i][j + 1].num == 1)) {
                            p3.add(s.boardRenderer[i][j]);
                        }
                    }
                    if (s.board[i][j].num == 3) {
                        if ((i > 0 && s.board[i - 1][j].type != type && s.board[i - 1][j].num == 2) ||
                                (j > 0 && s.board[i][j - 1].type != type && s.board[i][j - 1].num == 2) ||
                                (i < 7 && s.board[i + 1][j].type != type && s.board[i + 1][j].num == 2) ||
                                (j < 7 && s.board[i][j + 1].type != type && s.board[i][j + 1].num == 2)) {
                            p4.add(s.boardRenderer[i][j]);
                        }
                    }
                    if (s.board[i][j].num == 2) {
                        if ((i > 0 && s.board[i - 1][j].type != type && s.board[i - 1][j].num == 1) ||
                                (j > 0 && s.board[i][j - 1].type != type && s.board[i][j - 1].num == 1) ||
                                (i < 7 && s.board[i + 1][j].type != type && s.board[i + 1][j].num == 1) ||
                                (j < 7 && s.board[i][j + 1].type != type && s.board[i][j + 1].num == 1)) {
                            p5.add(s.boardRenderer[i][j]);
                        }
                    }
                    if (s.board[i][j].num == 3) {
                        if ((i > 0 && s.board[i - 1][j].type != type && s.board[i - 1][j].num == 1) ||
                                (j > 0 && s.board[i][j - 1].type != type && s.board[i][j - 1].num == 1) ||
                                (i < 7 && s.board[i + 1][j].type != type && s.board[i + 1][j].num == 1) ||
                                (j < 7 && s.board[i][j + 1].type != type && s.board[i][j + 1].num == 1)) {
                            p6.add(s.boardRenderer[i][j]);
                        }
                    }
                }
            }
        }

        if (p1.notEmpty())
            randomClick(p1);
        else if (p2.notEmpty())
            randomClick(p2);
        else if (p3.notEmpty())
            randomClick(p3);
        else if (p4.notEmpty())
            randomClick(p4);
        else if (p5.notEmpty())
            randomClick(p5);
        else if (p6.notEmpty())
            randomClick(p6);
        else
            randomClick(chips);

    }

    private void randomClick(Array<ChipRenderer> a) {

        if (a.notEmpty())
            a.get((int) (Math.random() * a.size)).click();

        clearAll();

    }

    private void clearAll() {
        chips.clear();
        p1.clear();
        p2.clear();
        p3.clear();
        p4.clear();
        p5.clear();
        p6.clear();
    }

}
