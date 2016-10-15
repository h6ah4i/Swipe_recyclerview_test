package com.avenger.avengerapp.presentation.presenters

/**
 * Created by RobGThai on 10/3/16.
 */
interface BasePresenter {
    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onResume() method.
     */
    fun resume()

    /**
     * Method that controls the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onPause() method.
     */
    fun pause()

    /**
     * Method that controls the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onStop() method.
     */
    fun stop()

    /**
     * Method that control the lifecycle of the view. It should be called in the view's
     * (Activity or Fragment) onDestroy() method.
     */
    fun destroy()


    /**
     * Method that should signal the appropriate view to show the appropriate error with the provided message.
     */
    fun onError(message: String)
}