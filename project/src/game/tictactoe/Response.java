package game.tictactoe;

class Response {
    public String info;
    public boolean hasAnError;

    public Response(boolean hasAnError, String info) {
        this.info = info;
        this.hasAnError = hasAnError;
    }

    @Override
    public String toString() {
        return info;
    }
}
