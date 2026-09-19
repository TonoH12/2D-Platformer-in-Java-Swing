public class Constants {

    public static class Actions {
        public static final int IDLE_LEFT = 0;
        public static final int IDLE_RIGHT = 1;
        public static final int RUN_LEFT = 2;
        public static final int RUN_RIGHT = 3;
        public static final int JUMP_LEFT = 4;
        public static final int JUMP_RIGHT = 5;
        public static final int CROUCH_LEFT = 6;
        public static final int CROUCH_RIGHT = 7;
    }

    public static class Directions {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class State {
        public static final int OFF = 0;
        public static final int ON = 1;
    }

    public static int getSpriteValue(int playerAction) {
        switch(playerAction) {
            case Actions.IDLE_LEFT: 
            case Actions.IDLE_RIGHT: 
                return 3;
            case Actions.RUN_LEFT:
            case Actions.RUN_RIGHT: 
                return 4;
            case Actions.JUMP_LEFT: 
            case Actions.JUMP_RIGHT: 
            case Actions.CROUCH_LEFT: 
            case Actions.CROUCH_RIGHT: 
            default:
                return 1;
        }
    }
}