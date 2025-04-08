package io.github.alfonsokevin.design1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @description: 测试两阶段终止
 * @create: 2025-04-08 17:16
 * @author: TangZhiKai
 **/
@Slf4j(topic = "c.TestMoitorThread")
public class TestMoitorThread {
    public static void main(String[] args) throws InterruptedException {
        TwoPhraseTermination twoPhraseTermination = new TwoPhraseTermination();
        //主线程启动监控线程
        twoPhraseTermination.start();

        //睡眠模拟，运行一会后，被主线程中断，看看能否优雅关闭
        TimeUnit.SECONDS.sleep(5);
        twoPhraseTermination.stop();
    }
}
