package com.example.huntergame;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    private ImageView[] game_IMG_hearts;
    private ImageView[][] game_IMG_imgs;
    private MaterialTextView game_LBL_score;
    private ImageButton[] game_BTN_arrows;
    private GameManager gameManager;
    private Player player,hunter;
    private int direction = -1;
    private boolean move = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameManager = new GameManager();
        player = new Player(4,1,"Player");
        hunter = new Player(0,1,"Hunter");

        findViews();
        
        // up
        game_BTN_arrows[0].setOnClickListener(e -> direction=0);
        // down
        game_BTN_arrows[1].setOnClickListener(e -> direction=1);
        // right
        game_BTN_arrows[2].setOnClickListener(e -> direction=2);
        // left
        game_BTN_arrows[3].setOnClickListener(e -> direction=3);

        startScoreTimer();
    }

    private void selectedPlayerDirection() {
        chooseDirection(player,direction);
    }

    private void randomHunterDirection() {
        chooseDirection(hunter,(int) (Math.random() * 4));
    }

    private void chooseDirection(Player p,int d) {
        switch (d) {
            case 0:
                moveUp(p);
                break;
            case 1:
                moveDown(p);
                break;
            case 2:
                moveRight(p);
                break;
            case 3:
                moveLeft(p);
                break;
            default:
                break;
        }
    }

    private void moveLeft(Player p) {
        if (p.getY() > 0) {
            game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_launcher_background);
            p.setY(p.getY() - 1);
            if(p.getName().equalsIgnoreCase("Player"))
                game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_spiderman);
            else
                game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_goblin);
            updateUI();
        }
    }

    private void moveRight(Player p) {
        if (p.getY() < 2) {
            game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_launcher_background);
            p.setY(p.getY() + 1);
            if(p.getName().equalsIgnoreCase("Player"))
                game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_spiderman);
            else
                game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_goblin);
            updateUI();
            }
    }

    private void moveDown(Player p) {
        if (p.getX() < 4) {
            game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_launcher_background);
            p.setX(p.getX() + 1);
            if(p.getName().equalsIgnoreCase("Player"))
                game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_spiderman);
            else
                game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_goblin);
            updateUI();
        }
    }

    private void moveUp(Player p) {
        if (p.getX() > 0) {
            game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_launcher_background);
            p.setX(p.getX() - 1);
            if(p.getName().equalsIgnoreCase("Player"))
                game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_spiderman);
            else
                game_IMG_imgs[p.getX()][p.getY()].setImageResource(R.drawable.ic_goblin);
            updateUI();
        }
    }

    private void updateUI() {
        if(player.getX() == hunter.getX() && player.getY() == hunter.getY()) {
            direction = -1;
            gameManager.reduceLives();
            gameManager.setScore();
            updateBoardUI();
        }
        game_LBL_score.setText("" + gameManager.getScore());

        for (int i = 0; i < game_IMG_hearts.length; i++)
            game_IMG_hearts[i].setVisibility(gameManager.getLives() > i ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateBoardUI() {
        if(gameManager.isDead()) {
            game_IMG_imgs[player.getX()][player.getY()].setImageResource(R.drawable.ic_grave);
            Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
            finishGame();
            return;
        }
        else
            game_IMG_imgs[player.getX()][player.getY()].setImageResource(R.drawable.ic_launcher_background);
        player.setX(4);
        player.setY(1);
        hunter.setX(0);
        hunter.setY(1);
        game_IMG_imgs[player.getX()][player.getY()].setImageResource(R.drawable.ic_spiderman);
        game_IMG_imgs[hunter.getX()][hunter.getY()].setImageResource(R.drawable.ic_goblin);
    }

    private void findViews() {
        game_IMG_imgs = new ImageView[][] {
                {findViewById(R.id.game_IMG_img1), findViewById(R.id.game_IMG_img2), findViewById(R.id.game_IMG_img3)},
                {findViewById(R.id.game_IMG_img4), findViewById(R.id.game_IMG_img5), findViewById(R.id.game_IMG_img6)},
                {findViewById(R.id.game_IMG_img7), findViewById(R.id.game_IMG_img8), findViewById(R.id.game_IMG_img9)},
                {findViewById(R.id.game_IMG_img10), findViewById(R.id.game_IMG_img11), findViewById(R.id.game_IMG_img12)},
                {findViewById(R.id.game_IMG_img13), findViewById(R.id.game_IMG_img14), findViewById(R.id.game_IMG_img15)}
        };
        game_IMG_hearts = new ImageView[] {
                findViewById(R.id.game_IMG_heart1),
                findViewById(R.id.game_IMG_heart2),
                findViewById(R.id.game_IMG_heart3)
        };
        game_BTN_arrows = new ImageButton[] {
                findViewById(R.id.game_BTN_up),
                findViewById(R.id.game_BTN_down),
                findViewById(R.id.game_BTN_right),
                findViewById(R.id.game_BTN_left)
        };
        game_LBL_score = findViewById(R.id.game_LBL_score);
    }

    private void finishGame() {
        finish();
    }

    private class Player {
        private int x,y;
        private String name;
        public Player(int x,int y,String name) { this.x=x; this.y=y; this.name=name; };
        public void setX(int x) { this.x=x; }
        public int getX() { return x; }
        public void setY(int y) { this.y=y; }
        public int getY() { return y; }
        public String getName() { return name; }
    }

    // ---------- ---------- TIMER ---------- ----------

    private final int DELAY = 1000;
    private enum TIMER_STATUS {
    OFF,
    RUNNING,
    PAUSE
    }
    private TIMER_STATUS timerStatus = TIMER_STATUS.OFF;
    private Timer timer;

    private void startScoreTimer() {
        if (timerStatus == TIMER_STATUS.RUNNING) {
            stopTimer();
            timerStatus = TIMER_STATUS.OFF;
        } else {
            startTimer();
        }
    }

    private void tick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                selectedPlayerDirection();
                if(!move) // disable movement at the first second( just for better view, not important )
                    move=true;
                else {
                    randomHunterDirection();
                    gameManager.addToScore();
                }
                game_LBL_score.setText("" + gameManager.getScore());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timerStatus == TIMER_STATUS.RUNNING) {
            stopTimer();
            timerStatus = TIMER_STATUS.PAUSE;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (timerStatus == TIMER_STATUS.PAUSE)
            startTimer();
        else
            gameManager.setScore();
    }

    private void startTimer() {
        timerStatus = TIMER_STATUS.RUNNING;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tick();
            }
        }, 0, DELAY);
    }

    private void stopTimer() {
        timer.cancel();
    }
}