package com.zio.fiber

import java.util.concurrent.Executors

object Main extends App {

  /**
   * Basic problem
   * JVM maps threads in 1-2-1 manner with OS threads
   */
  println("start")
  Thread.sleep(1000)
  println("end")

  /**
   * Concrete problem
   * Run 2 tasks concurrently with 1 thread
   */
  def task(id: Int): Runnable = () => {
    println(s"${Thread.currentThread().getName} start-$id")
    Thread.sleep(3000)
    println(s"${Thread.currentThread().getName} end-$id")
  }

  /**
   * Attempt #1
   * 1 task => 1 thread
   */
  new Thread(task(1)).start()

  /**
   * Attempt #2
   * 2 tasks => 1 thread
   */
  val oneThreadExecutor = Executors.newFixedThreadPool(1)
  (1 to 2).foreach(id =>
    oneThreadExecutor.execute(task(id))
  )

  /**
   * Attempt #3
   * 2 tasks => 2 threads
   */
  val twoThreadExecutor = Executors.newFixedThreadPool(2)
  (1 to 2).foreach(id =>
    twoThreadExecutor.execute(task(id))
  )
}
