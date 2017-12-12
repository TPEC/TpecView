package pers.tpec.games.colorbeans2;

import pers.tpec.games.colorbeans2.scenes.GameOverScene;
import pers.tpec.games.colorbeans2.scenes.MainScene;
import pers.tpec.games.colorbeans2.scenes.MenuScene;

public class GameScenes {
    private MainScene mainScene;
    private MenuScene menuScene;
    private GameOverScene gameOverScene;

    public MainScene getMainScene() {
        return mainScene;
    }

    public void setMainScene(MainScene mainScene) {
        this.mainScene = mainScene;
    }

    public MenuScene getMenuScene() {
        return menuScene;
    }

    public void setMenuScene(MenuScene menuScene) {
        this.menuScene = menuScene;
    }

    public GameOverScene getGameOverScene() {
        return gameOverScene;
    }

    public void setGameOverScene(GameOverScene gameOverScene) {
        this.gameOverScene = gameOverScene;
    }

    private static GameScenes ourInstance = new GameScenes();

    public static GameScenes getInstance() {
        return ourInstance;
    }

    private GameScenes() {
    }
}
