package com.newmaster.reggie;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class ReggieApplication {

    public static void main(String[] args) {
        /***
         *                    _ooOoo_
         *                   o8888888o
         *                   88" . "88
         *                   (| -_- |)
         *                    O\ = /O
         *                ____/`---'\____
         *              .   ' \\| |// `.
         *               / \\||| : |||// \
         *             / _||||| -:- |||||- \
         *               | | \\\ - /// | |
         *             | \_| ''\---/'' | |
         *              \ .-\__ `-` ___/-. /
         *           ___`. .' /--.--\ `. . __
         *        ."" '< `.___\_<|>_/___.' >'"".
         *       | | : `- \`.;`\ _ /`;.`/ - ` : | |
         *         \ \ `-. \_ __\ /__ _/ .-` / /
         * ======`-.____`-.___\_____/___.-`____.-'======
         *                    `=---='
         *
         * .............................................
         *          佛祖保佑             永无BUG
         */

        /***
         *  佛曰:
         *          写字楼里写字间，写字间里程序员；
         *          程序人员写程序，又拿程序换酒钱。
         *          酒醒只在网上坐，酒醉还来网下眠；
         *          酒醉酒醒日复日，网上网下年复年。
         *          但愿老死电脑间，不愿鞠躬老板前；
         *          奔驰宝马贵者趣，公交自行程序员。
         *          别人笑我忒疯癫，我笑自己命太贱；
         *          不见满街漂亮妹，哪个归得程序员？
         */

        SpringApplication.run(ReggieApplication.class, args);
        log.info("项目启动成功!");
    }

}
