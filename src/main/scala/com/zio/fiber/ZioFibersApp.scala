package com.zio.fiber

import zio._
import zio.console._
import zio.duration._
import zio.ZIO

/**
 * ZIO.sleep(), based on fibers (non-blocking), solves the problem of blocking Thread.sleep().
 * ZIO fibers are run in ZIO context, which adds its own fibers management layer
 * and therefore allows to utilize fibers in non-blocking manner.  
 *
 * Output:
 * zio-default-async-2 start-1
 * zio-default-async-3 start-2
 * zio-default-async-5 end-1
 * zio-default-async-6 end-2
 */
object ZioFibersApp extends zio.App {

  def zioTask(id: Int) =
    for {
      _ <- putStrLn(s"${Thread.currentThread().getName} start-$id")
      _ <- ZIO.sleep(3.seconds)
      _ <- putStrLn(s"${Thread.currentThread().getName} end-$id")
    } yield ()

  override def run(args: List[String]) = {
    ZIO
      .foreachPar(Seq(1, 2))(zioTask)
      .as(ExitCode.success)
  }
}
