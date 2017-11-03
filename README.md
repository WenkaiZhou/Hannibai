# Hannibal

一种简单优雅的方法来使用android SharePreference。

## 在项目中使用[Hannibai](https://github.com/xuehuayous/Hannibal) 

1. 如果您的项目使用 Gradle 构建, 只需要在您的build.gradle文件添加如下到 `dependencies` :

	```
	compile 'com.kevin:hannibai:0.3.0'
	annotationProcessor 'com.kevin:hannibai-compiler:0.3.1'
	```

2. 引入JSON序列化

	> 由于可以保存**对象**甚至**集合**到SharePreference，根据项目使用引入具体转换器。
	
	1. Gson

		```
		compile 'com.kevin:hannibai-converter-gson:0.2.6'
		```
	
	2. Jackson
	
		```
		compile 'com.kevin:hannibai-converter-jackson:0.2.6'
		```
		
	3. FastJson
    	
    	```
       compile 'com.kevin:hannibai-converter-fastjson:0.2.6'
       ```
	
3. 这里仅仅实现了Gson、Jackson及FastJson的实现，后续会扩展，或者你也可以扩展。

## 简单使用


1. 在Application 中初始化

	```
	Hannibai.init(this);
	if (debug) {
	    Hannibai.setDebug(true);
	}
	Hannibai.setConverterFactory(GsonConverterFactory.create());
	```

2. 创建一个类，使用`SharePreference`进行注解。

	```
	@SharePreference
	public class AppPreference {
	}
	```

3. 加入一个成员变量

	> 这里使用`public`修饰，当然你也可以使用`private`、`protected`或者不写，任何一种姿势都可以。

	```
	@SharePreference
	public class AppPreference {
	    public String name;
	}
	```

4. Build —> Rebuild Project

	> 这个过程会自动生成一堆你觉得写起来很恶心的东西。

5. 在代码中使用

	```
	 // 获取AppPreference操作类
    AppPreferenceHandle preferenceHandle = Hannibai.create(AppPreferenceHandle.class);

    // 设置name到SharePreference
    preferenceHandle.setName("Kevin");
    // 从SharePreference中获取name
    String name = preferenceHandle.getName();
    Toast.makeText(this, "name = " + name, Toast.LENGTH_SHORT).show();
	```

是不是跟简单的就完成了保存数据到`SharePreference`，已经从`SharePreference`读取数据，之前恶心的一堆东西已经替你偷偷生成而且藏起来啦~

## 进阶使用

1. 支持的类型

	| 类型 | sample |
	|---|---|
	| `String` | String name;|
	| `int` | int age;|
	| `Integer` | Integer age;|
	| `long` | long timestamp;|
	| `Long` | Long timestamp;|
	| `float` | float salary;|
	| `Float` | Float salary;|
	| `double` | double salary;|
	| `Double` | Double salary;|
	| `User` | User user;|
	| `List<xxx>` | List<User> userList;|
	| `Map<xxx, xxx>` | Map<String, User> userMap;|
	| `Set<xxx>` | Set<User> userSet;|
	| `XXX<xxx>` | 只支持一级泛型，`List<List<String>>` 这种是不支持的。|

1. 设置默认值

	```
	@DefString("zwenkai")
	public String name;
	```
	
	支持

	| 类型 | sample |
	|---|---|
	| DefString | @DefString("zwenkai")|
	| DefInt | @DefInt(18)|
	| DefBoolean | @DefBoolean(true)|
	| DefLong | @DefLong(123456789)|
	| DefFloat | @DefFloat(123.45F)|

2. 设置过期时间

	> 默认不会过期
	
	```
	@Expire(value = 3, unit = Expire.Unit.MINUTES)
	public long salary;
	```
	
	可以设置更新数据时重新倒计时：
	
	```
	@Expire(value = 5, unit = Expire.Unit.MINUTES, update = true)
	public long salary;
	```
	
	支持
	
	| 单位 | sample |
	|---|---|
	| 毫秒 | @Expire(value = 1, unit = Expire.Unit.MILLISECONDS)|
	| 秒 | @Expire(value = 1, unit = Expire.Unit.SECONDS)|
	| 分 | @Expire(value = 1, unit = Expire.Unit.MINUTES)|
	| 小时 | @Expire(value = 1, unit = Expire.Unit.HOURS)|
	| 天 | @Expire(value = 1, unit = Expire.Unit.DAYS)|
	
3. 设置提交类型

	> 提交类型有`Commit`和`Apply`两种，默认为`Apply`。
	
	1. Commit
	
		```
		@Commit
		public String userName;
		```

	2. Apply
	
		```
		@Apply
		public String userName;
		```
	
4. 获取操作类

	> 有两种方式，一种是获取通用的，另一种是可以传入ID参数，为不同用户建立不同`SharePreference`文件。

	1. 通用
	
		```
		testPreference = Hannibai.create(TestPreferenceHandle.class);
		```
	2. 区分用户
	
		```
		testPreference = Hannibai.create(TestPreferenceHandle.class, "ID_123");
		```
	
## 更多

> 请参考项目中示例
	
# 混淆

> 如果使用了混淆，添加如下到混淆配置文件

```
-dontwarn com.kevin.hannibai.**
-keep class com.kevin.hannibai.** { *; }
-keep class * implements com.kevin.hannibai.IHandle { *; }
-keep @com.kevin.hannibai.annotation.SharePreference class * { *; }
```