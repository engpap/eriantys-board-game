package it.polimi.ingsw.triton.launcher.utils.obs;

public interface Observer<T> {
    void update(T message);
}
