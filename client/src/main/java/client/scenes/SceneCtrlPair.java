package client.scenes;

import javafx.scene.Parent;

public class SceneCtrlPair {
    private Parent scene;
    private AbstractCtrl ctrl;

    /**
     * Constructor for SceneCtrlPair
     * @param scene
     * @param ctrl
     */
    public SceneCtrlPair(Parent scene, AbstractCtrl ctrl) {
        this.scene = scene;
        this.ctrl = ctrl;
    }

    /**
     * Getter for Scene
     * @return scene
     */
    public Parent getScene() {
        return scene;
    }

    /**
     * Setter for Scene
     * @param scene
     */
    public void setScene(Parent scene) {
        this.scene = scene;
    }

    /**
     * Getter for Controller
     * @return ctrl
     */
    public AbstractCtrl getCtrl() {
        return ctrl;
    }

    /**
     * Getter for Controller
     * @param ctrl
     */
    public void setCtrl(AbstractCtrl ctrl) {
        this.ctrl = ctrl;
    }
}
