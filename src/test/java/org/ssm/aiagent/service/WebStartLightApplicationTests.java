package org.ssm.aiagent.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WebStartLightApplicationTests {

    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Autowired
    private OpenAiChatModel reasonerModel;

    @Test
    public void test_base() {
        TestAiService testAiService = AiServices.create(TestAiService.class, reasonerModel);
        Result<User> result = testAiService.parseUserInfo("我叫小王，今年18岁，来自中国");
        // 消耗的token
        System.out.println("消耗的token：" + result.tokenUsage());
        // 模型回答
        System.out.println("模型回答：" + result.finalResponse().toString());
        // 模型中间回答
        System.out.println("模型中间回答：" + result.intermediateResponses());
        // 结构化对象
        System.out.println("结构化对象：" + result.content());
    }

    // 测试查询语句1: 包含所有基本信息的用户
    @Test
    public void testQueryStatement1() {
        TestAiService testAiService = AiServices.create(TestAiService.class, reasonerModel);
        // 测试输入包含用户的基本信息：姓名、年龄和电子邮件
        Result<User> result = testAiService.parseUserInfo("你好，我叫李明，今年25岁了，你可以通过 li ming@example.com 联系到我。");

        User user = result.content();
        System.out.println("=== 测试查询语句1结果 ===");
        System.out.println("解析得到的用户信息：" + JSON.toJSONString(user, JSONWriter.Feature.PrettyFormat));
        System.out.println("消耗的token：" + result.tokenUsage());
        System.out.println("模型最终回答：" + result.finalResponse());

        // 验证基本字段
        assert user.getName() != null : "用户姓名不应为空";
        assert user.getName().equals("李明") : "用户姓名应为'李明'";
        assert user.getAge() != null : "用户年龄不应为空";
        assert user.getAge() == 25 : "用户年龄应为25";
        assert user.getEmail() != null : "用户邮箱不应为空";
        assert user.getEmail().equals("li ming@example.com") : "用户邮箱应为'li ming@example.com'";

        // 验证其他字段为空
        assert user.getAddress() == null : "用户地址应为空";
        assert user.getTags() == null : "用户标签应为空";
        assert user.getFriends() == null : "用户朋友列表应为空";

        System.out.println("✅ 测试查询语句1通过：基本用户信息提取成功");
    }

    // 测试查询语句2: 包含地址信息的用户
    @Test
    public void testQueryStatement2() {
        TestAiService testAiService = AiServices.create(TestAiService.class, reasonerModel);
        // 测试输入包含用户的地址信息：国家、城市和街道
        Result<User> result = testAiService.parseUserInfo("我是张伟，目前居住在中国广东省深圳市南山区科技园南路1001号。");

        User user = result.content();
        System.out.println("=== 测试查询语句2结果 ===");
        System.out.println("解析得到的用户信息：" + JSON.toJSONString(user, JSONWriter.Feature.PrettyFormat));
        System.out.println("消耗的token：" + result.tokenUsage());
        System.out.println("模型最终回答：" + result.finalResponse());

        // 验证姓名
        assert user.getName() != null : "用户姓名不应为空";
        assert user.getName().equals("张伟") : "用户姓名应为'张伟'";

        // 验证地址字段
        assert user.getAddress() != null : "用户地址不应为空";
        assert user.getAddress().getCountry() != null : "地址国家信息不应为空";
        assert user.getAddress().getCountry().equals("中国") : "地址国家应为'中国'";
        assert user.getAddress().getCity() != null : "地址城市信息不应为空";
        assert user.getAddress().getCity().equals("深圳市") : "地址城市应为'深圳市'";
        assert user.getAddress().getStreet() != null : "地址街道信息不应为空";
        assert user.getAddress().getStreet().equals("南山区科技园南路1001号") : "地址街道应为'南山区科技园南路1001号'";

        System.out.println("✅ 测试查询语句2通过：地址信息提取成功");
    }

    // 测试查询语句3: 包含标签和朋友列表的用户
    @Test
    public void testQueryStatement3() {
        TestAiService testAiService = AiServices.create(TestAiService.class, reasonerModel);
        // 测试输入包含用户的兴趣爱好标签和朋友列表
        Result<User> result = testAiService.parseUserInfo(
                "我是一个热爱编程和摄影的软件工程师，平时喜欢户外运动。我的好朋友有王芳和刘强，我们经常一起参加技术交流会。");

        User user = result.content();
        System.out.println("=== 测试查询语句3结果 ===");
        System.out.println("解析得到的用户信息：" + JSON.toJSONString(user, JSONWriter.Feature.PrettyFormat));
        System.out.println("消耗的token：" + result.tokenUsage());
        System.out.println("模型最终回答：" + result.finalResponse());

        // 验证标签列表
        assert user.getTags() != null : "用户标签不应为空";
        assert user.getTags().size() >= 3 : "用户标签数量应不少于3个";
        assert user.getTags().contains("编程") : "用户标签应包含'编程'";
        assert user.getTags().contains("摄影") : "用户标签应包含'摄影'";
        assert user.getTags().contains("户外运动") : "用户标签应包含'户外运动'";

        // 验证朋友列表
        assert user.getFriends() != null : "用户朋友列表不应为空";
        assert user.getFriends().size() >= 2 : "用户朋友数量应不少于2个";

        boolean hasWangFang = false;
        boolean hasLiuQiang = false;
        for (User friend : user.getFriends()) {
            if ("王芳".equals(friend.getName())) {
                hasWangFang = true;
            }
            if ("刘强".equals(friend.getName())) {
                hasLiuQiang = true;
            }
        }
        assert hasWangFang : "用户朋友应包含'王芳'";
        assert hasLiuQiang : "用户朋友应包含'刘强'";

        System.out.println("✅ 测试查询语句3通过：标签和朋友列表提取成功");
    }

    // 测试查询语句4: 完整信息的用户
    @Test
    public void testQueryStatement4() {
        TestAiService testAiService = AiServices.create(TestAiService.class, reasonerModel);
        // 测试输入包含用户的所有信息：基本资料、地址、标签和朋友
        Result<User> result = testAiService.parseUserInfo(
                "我叫陈小红，今年28岁，邮箱是chenxiaohong@example.com。我住在美国加利福尼亚州旧金山市市场街2000号。我是一个UI设计师、摄影师和旅行爱好者。我的朋友有David和Sophia，他们都是我的同事。");

        User user = result.content();
        System.out.println("=== 测试查询语句4结果 ===");
        System.out.println("解析得到的用户信息：" + JSON.toJSONString(user, JSONWriter.Feature.PrettyFormat));
        System.out.println("消耗的token：" + result.tokenUsage());
        System.out.println("模型最终回答：" + result.finalResponse());

        // 验证基本字段
        assert user.getName() != null : "用户姓名不应为空";
        assert user.getName().equals("陈小红") : "用户姓名应为'陈小红'";
        assert user.getAge() != null : "用户年龄不应为空";
        assert user.getAge() == 28 : "用户年龄应为28";
        assert user.getEmail() != null : "用户邮箱不应为空";
        assert user.getEmail().equals("chenxiaohong@example.com") : "用户邮箱应为'chenxiaohong@example.com'";

        // 验证地址字段
        assert user.getAddress() != null : "用户地址不应为空";
        assert user.getAddress().getCountry() != null : "地址国家信息不应为空";
        assert user.getAddress().getCountry().equals("美国") : "地址国家应为'美国'";
        assert user.getAddress().getCity() != null : "地址城市信息不应为空";
        assert user.getAddress().getCity().equals("旧金山市") : "地址城市应为'旧金山市'";
        assert user.getAddress().getStreet() != null : "地址街道信息不应为空";
        assert user.getAddress().getStreet().equals("市场街2000号") : "地址街道应为'市场街2000号'";

        // 验证标签
        assert user.getTags() != null : "用户标签不应为空";
        assert user.getTags().size() >= 3 : "用户标签数量应不少于3个";
        assert user.getTags().contains("UI设计师") : "用户标签应包含'UI设计师'";
        assert user.getTags().contains("摄影师") : "用户标签应包含'摄影师'";
        assert user.getTags().contains("旅行爱好者") : "用户标签应包含'旅行爱好者'";

        // 验证朋友列表
        assert user.getFriends() != null : "用户朋友列表不应为空";
        assert user.getFriends().size() >= 2 : "用户朋友数量应不少于2个";

        boolean hasDavid = false;
        boolean hasSophia = false;
        for (User friend : user.getFriends()) {
            if ("David".equals(friend.getName())) {
                hasDavid = true;
            }
            if ("Sophia".equals(friend.getName())) {
                hasSophia = true;
            }
        }
        assert hasDavid : "用户朋友应包含'David'";
        assert hasSophia : "用户朋友应包含'Sophia'";

        System.out.println("✅ 测试查询语句4通过：完整用户信息提取成功");
    }

}