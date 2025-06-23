package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main implements ApplicationListener {
    private Stage stage;
    private BucketActor bucket;
    private Array<DropActor> drops;
    private Texture bucketTexture;
    private Texture dropTexture;
    private float dropTimer;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        bucketTexture = new Texture(Gdx.files.internal("bucket.png"));
        dropTexture = new Texture(Gdx.files.internal("drop.png"));

        bucket = new BucketActor();
        bucket.setTexture(bucketTexture);
        bucket.setPosition(Gdx.graphics.getWidth() / 2f - bucket.getWidth() / 2f, 20);
        stage.addActor(bucket);

        drops = new Array<>();
        dropTimer = 0f;
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        dropTimer += dt;
        if (dropTimer > 1f) {
            dropTimer = 0f;
            spawnDrop();
        }

        // Обновляем актёров
        stage.act(dt);

        // Проверяем столкновения и удаляем капли
        for (int i = drops.size - 1; i >= 0; i--) {
            DropActor drop = drops.get(i);
            if (drop.getY() + drop.getHeight() < 0) {
                drop.remove();
                drops.removeIndex(i);
            } else if (drop.overlaps(bucket)) {
                drop.remove();
                drops.removeIndex(i);
                // Здесь можно добавить звук или счётчик пойманных капель
            }
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();
    }

    private void spawnDrop() {
        DropActor drop = new DropActor();
        drop.setTexture(dropTexture);
        float x = (float) Math.random() * (Gdx.graphics.getWidth() - drop.getWidth());
        drop.setPosition(x, Gdx.graphics.getHeight());
        stage.addActor(drop);
        drops.add(drop);
    }

    @Override
    public void dispose() {
        stage.dispose();
        bucketTexture.dispose();
        dropTexture.dispose();
    }
}
