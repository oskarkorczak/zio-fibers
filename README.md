# ZIO Fibers
This repo is based on examples shown in ZIO non-blocking sleep [blog post][blog-post]. 

ZIO is a functional library for asynchronous and concurrent programming.

It could be used for non-blocking (asynchronous) sleep.

## Problem statement
JVM threads are blocking (synchronous). 


### JVM to OS thread mapping

JVM uses a 1-2-1 mapping between Java and kernel threads. JVM asks the OS to give up the thread’s `rights` to the CPU for the specified time. 

The critical point here is that the sleeping thread is completely taken out and is not reusable while sleeping.

### Limitations
Here are few important limitations that come with JVM threads:

* There is a limit to how many threads you can create. After around `30k`, you will get this error: `java.lang.OutOfMemoryError : unable to create new native Thread`
* JVM Threads can be expensive memory-wise to create, as they come with a dedicated stack
* Too many JVM threads will incur overhead because of expensive context switches and the way they share finite hardware resources

### Blocking nature
JVM threads are blocking (synchronous) in nature, which means that sleeping thread is in`java.lang.Thread.State: TIMED_WAITING (sleeping)` state. It effectively means that thread exists, but is is not working on behalf of task which put it asleep and not for any other task in entire OS. 

Using `Executors` doesn't change the situation, as the thread pool is still based on JVM threads, which are blocking. 

## Solution

[ZIO's concurrency][zio-docs] is built on `fibers`, which are lightweight `green threads` implemented by the **ZIO runtime system**.

Unlike operating system threads, fibers consume almost no memory, have growable and shrinkable stacks, don't waste resources blocking, and will be garbage collected automatically if they are suspended and unreachable.

Fibers are **scheduled by the ZIO runtime** and will cooperatively yield to each other, which enables multitasking, even when operating in a single-threaded environment (like JavaScript, or even the JVM when configured with one thread).

All effects in ZIO are executed by some fiber. If you did not create the fiber, then the fiber was created by some operation you are using (if the operation is concurrent or parallel), or by the ZIO runtime system.

Even if you only write *single-threaded* code, with no parallel or concurrent operations, then there will be at least one fiber: the *main* fiber that executes your effect.


## Summary

* Each JVM Thread maps to an OS thread, in a 1-2-1 fashion. And this is the root of a lot of problems.
* `Thread.sleep` is bad! It blocks the current thread and renders it unusable for further work.
* [Project Loom][loom] (that will be available in JDK 17) will solve a lot of issues.
* You can use ScheduledExecutorService to achieve non-blocking sleep.
* You can use Functional Programming to model a language where doing sleep is non-blocking.
* The ZIO library provides a non-blocking sleep out of the box.



[blog-post]:https://www.freecodecamp.org/news/non-blocking-thread-sleep-on-jvm/#b21d0dad-e788-434d-984a-0b0aa6e65454
[zio-docs]:https://zio.dev/docs/overview/overview_basic_concurrency
[loom]:https://wiki.openjdk.java.net/display/loom/Main