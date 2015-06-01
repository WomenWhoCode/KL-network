package wwckl.projectmiki.asyncTask;

/**
 * Created by lydialim on 6/1/15.
 *
 * Interface to be used by all the async task methods
 */
public interface IAsyncTaskListener<T> {

    /**
     * Method to be implemented when background process starts
     */
    void processOnStart ();

    /**
     * Method to be implemented when background process completes
     */
    void processOnComplete (T result);
}