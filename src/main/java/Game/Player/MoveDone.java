package Game.Player;

public enum MoveDone {
    SUCCESS {
        @Override
        public boolean isSuccessful() {
            return true;
        }
    },
    ILLEGAL_MOVE {
        @Override
        public boolean isSuccessful() {
            return false;
        }
    },
    PLAYER_IN_CHECK {
        @Override
        public boolean isSuccessful() {
            return false;
        }
    };

    public abstract boolean isSuccessful();
}
