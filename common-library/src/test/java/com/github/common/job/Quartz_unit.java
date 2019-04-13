package com.github.common.job;

import org.junit.Test;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

import java.time.LocalDateTime;

/**
 * @author alex.chen
 * @Description:
 * @date 2018/4/13
 */
public class Quartz_unit {
    private JobDetail job = JobBuilder.newJob(HelloJob.class).withIdentity("myjob1", "mygroup1").build();

    @Test
    public void simpleTrigger() throws SchedulerException, InterruptedException {
        System.out.println("启动时间: " + LocalDateTime.now().toString());
        // 五秒后启动一次
        //Trigger trigger = TriggerBuilder.newTrigger().withIdentity("mytrigger1", "mygroup1").startAt(new Date()).build();

        // 每5秒调用一次
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("mytrigger2", "mygroup2")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                .build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
        // 30 秒后关闭
        Thread.sleep(30_000);
        scheduler.shutdown(true);
    }

    @Test
    public void cronTrigger() throws SchedulerException, InterruptedException {
        // 每5秒调用一次
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity("mytrigger2", "mygroup2")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
        // 30 秒后关闭
        Thread.sleep(30_000);
        scheduler.shutdown(true);
    }

    @Test
    public void addJobListener() throws SchedulerException {
        JobKey jobKey = new JobKey("dummyJobName", "group1");
        JobDetail job = JobBuilder.newJob(ExceptionJob.class).withIdentity(jobKey).build();
        Trigger trigger = TriggerBuilder
                .newTrigger()
                .withIdentity("dummyTriggerName", "group1")
                .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
                .build();
        Scheduler scheduler = new StdSchedulerFactory().getScheduler();
        //Listener attached to jobKey
        scheduler.getListenerManager().addJobListener(
                new HelloJobListener(), KeyMatcher.keyEquals(jobKey)
        );

        //Listener attached to group named "group 1" only.
        //scheduler.getListenerManager().addJobListener(
        //	new HelloJobListener(), GroupMatcher.jobGroupEquals("group1")
        //);
        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

    public static class HelloJob implements Job {
        @Override
        public void execute(JobExecutionContext context) {
            JobDetail jobDetail = context.getJobDetail();
            JobKey key = jobDetail.getKey();
            System.out.println("当前 Job Key: " + key + ", 当前时间: " + LocalDateTime.now().toString());
        }
    }

    public class ExceptionJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("五秒一次的体验...");
            throw new JobExecutionException("异常消息: XXX出错了");
        }
    }

    public class HelloJobListener implements JobListener {
        public static final String LISTENER_NAME = "dummyJobListenerName";

        @Override
        public String getName() {
            return LISTENER_NAME;
        }

        /**
         * 当任务即将执行时
         *
         * @param context
         */
        @Override
        public void jobToBeExecuted(JobExecutionContext context) {
            String jobName = context.getJobDetail().getKey().toString();
            System.out.println("任务要开始啦");
            System.out.println("任务 : " + jobName + " 开始...");

        }

        @Override
        public void jobExecutionVetoed(JobExecutionContext context) {
            System.out.println("jobExecutionVetoed");
        }

        /**
         * 在执行任务后运行此操作
         *
         * @param context
         * @param jobException
         */
        @Override
        public void jobWasExecuted(JobExecutionContext context,
                                   JobExecutionException jobException) {
            System.out.println("执行任务后运行");
            String jobName = context.getJobDetail().getKey().toString();
            System.out.println("任务 : " + jobName + " 运行结束...");

            if (!jobException.getMessage().equals("")) {
                System.out.println("Exception thrown by: " + jobName + " Exception: " + jobException.getMessage());
            }

        }
    }
}
