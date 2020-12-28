package com.zio.fiber

import zio._
import zio.console._
import zio.duration._
import zio.ZIO

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
