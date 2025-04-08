package io.github.alfonsokevin.design1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description: 两阶段终止-设计模式
 * 确保一个线程可以被另一个线程优雅的关闭，而不是stop或者是System.exit(0)
 * 优雅是指，确保线程被关闭前可以有机会处理一些逻辑（料理后事）
 * 场景：现在需要设计一个监控线程，监控程序运行时的一些信息。当被其他线程中断后，需要优雅关闭。
 * @create: 2025-04-08 17:10
 * @author: TangZhiKai
 **/
@Slf4j(topic = "c.TwoPhraseTermination")
public class TwoPhraseTermination {
    /**
     * monitor 监控线程
     */
    private Thread monitor;

    /**
     * 启动监控线程
     */
    public void start(){
        monitor = new Thread(()->{
            while(true){
                //1.监控线程循环执行，获取中断标记
                Thread currentThread = Thread.currentThread();
                //2.如果存在中断的标记，那么终止监控线程，循环结束
                boolean isInterrupted = currentThread.isInterrupted();
                if(isInterrupted){
                    log.info("{} 被中断~~",currentThread.getName());
                    //料理后事 ...
                    //...
                    break;
                }
                //3.如果没有，那么则休眠一会获取监控的信息
                try {
                    TimeUnit.SECONDS.sleep(1); //如果这里被中断，会抛出中断异常
                    //执行获取监控信息的代码..
                    //如果下面被中断了，即运行时中断，中断标记就是true，下一轮就会结束
                    log.info("{} 监控线程获取信息中~~",currentThread.getName());
                } catch (InterruptedException e) {
                    //4.如果休眠被打断，那么中断标记将会被重置为false，需要手动打断
                    log.error("{} 线程休眠被中断了~~",e);
                    //手动打断重置为false的标记,此时线程是RUNNABLE，运行时打断，打断标记为true
                    //可以测试如果不加，会是怎么样的
                    monitor.interrupt();
                }
            }
        },"monitor");

        monitor.start();
    }

    /**
     * 其他线程中断监控线程
     */
    public void stop(){
        monitor.interrupt();
    }

}
