package com.zio.fiber

import java.util.concurrent.Executors

object Main extends App {

  /**
   * Basic Problem:
   * JVM maps threads in 1-2-1 manner with OS threads.
   *
   * Output:
   * Thread-0 start-1
   * Thread-0 end-1
   */
  println("start")
  Thread.sleep(1000)
  println("end")

  /**
   * Concrete Problem:
   * Run 2 tasks concurrently with 1 thread
   */
  def task(id: Int): Runnable = () => {
    println(s"${Thread.currentThread().getName} start-$id")
    Thread.sleep(3000)
    println(s"${Thread.currentThread().getName} end-$id")
  }

  /**
   * Attempt #1:
   * 1 task => 1 thread
   *
   * Output:
   * Thread-0 start-1
   * Thread-0 end-1
   */
  new Thread(task(1)).start()

  /**
   * Attempt #2:
   * 2 tasks => 1 thread
   *
   * Output:
   * pool-1-thread-1 start-1
   * pool-1-thread-1 end-1
   * pool-1-thread-1 start-2
   * pool-1-thread-1 end-2
   */
  val oneThreadExecutor = Executors.newFixedThreadPool(1)
  (1 to 2).foreach(id =>
    oneThreadExecutor.execute(task(id))
  )

  /**
   * Attempt #3:
   * 2 tasks => 2 threads
   *
   * Output:
   * pool-1-thread-2 start-2
   * pool-1-thread-1 start-1
   * pool-1-thread-2 end-2
   * pool-1-thread-1 end-1
   */
  val twoThreadExecutor = Executors.newFixedThreadPool(2)
  (1 to 2).foreach(id =>
    twoThreadExecutor.execute(task(id))
  )
}
