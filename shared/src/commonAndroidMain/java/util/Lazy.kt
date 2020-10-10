package util

/**
 * Created by Phong Huynh on 10/6/2020
 */
fun interface Lazy<V> {
    fun get(): V
}