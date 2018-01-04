# Hannibal

一种简单优雅的方法来使用android SharePreference。

## 在项目中使用[Hannibai](https://github.com/xuehuayous/Hannibal) 

1. 如果您的项目使用 Gradle 构建, 只需要在您的build.gradle文件添加如下到 `dependencies` :

	```
	compile 'com.kevin:hannibai:0.5.1'
	annotationProcessor 'com.kevin:hannibai-compiler:0.5.1'
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
		
	4. LoganSquare
        	
        ```
    	compile 'com.kevin:hannibai-converter-logansquare:0.2.6'
    	```
	
3. 这里仅仅实现了Gson、Jackson、FastJson及LoganSquare的实现，后续会扩展，或者你也可以扩展。

## 简单使用


1. 在Application 中初始化

	```
	Hannibai.init(this);
	if (debug) {
	    Hannibai.setDebug(true);
	}
	Hannibai.setConverterFactory(GsonConverterFactory.create());
	```

2. 创建一个类，使用`SharePreference`进行注解

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
	
	如果项目很庞大，build一次时间比较长，可以使用命令：
	
	```
	// app 换成对应的module名
	./gradlew app:compileDebugJavaWithJavac
	```

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

1. 初始化

	> 在进行初始化的时候有如下两个方法

	```
	Hannibai.init(this);
	```
	```
	Hannibai.init(this, false);
	```
	
	这两个方法的区别就是是否对数据进行加密，如果第二个参数为`true`则代表要进行加密存储。默认为`true`，这样可以减小文件的大小。

2. 获取操作类

	> 有两种方式，一种是获取通用的，另一种是可以传入ID参数，为不同用户建立不同`SharePreference`文件。

	假设类为`AppPreference`:

	1. 通用
	
		```
		AppPreferenceHandle preferenceHandle = Hannibai.create(AppPreferenceHandle.class);
		```
	2. 区分用户
	
		```
		AppPreferenceHandle preferenceHandle = Hannibai.create(AppPreferenceHandle.class, "ID_123");
		```

3. 生成的方法

	> 假设你的成员变量是`name`，那么会生成以下方法：
	
	1. 判断`SharePreference`中是否包含`name`

		```
		@DefString("")
		public boolean containsName() {
			return Hannibai.contains1(mSharedPreferencesName, mId, "name", "");
		}
		```
		
	2. 在`SharePreference`中获取`name`值

		```
		@DefString("")
		public String getName() {
			return Hannibai.get1(mSharedPreferencesName, mId, "name", "");
		}
		```
		
	3. 设置`name`值到`SharePreference`

		```
		@Apply
		public void setName(final String name) {
			Hannibai.set1(mSharedPreferencesName, mId, "name", -1L, false, name);
		}
		```
		
	4. 在`SharePreference`中移除`name`

		```
		@Apply
		public void removeName() {
			Hannibai.remove1(mSharedPreferencesName, mId, "name");
		}
		```
		
	5. 在`SharePreference`中移除所有数据

		```
		@Apply
		public void removeAll() {
			Hannibai.clear(mSharedPreferencesName, mId);
		}
		```

4. 支持的类型

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

5. 设置默认值

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

6. 设置过期时间

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
	
7. 设置提交类型

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
		
8. 支持RxJava

	> 有些时候，`Observable`对象更好操作，那么你只需要一个注解`@RxJava`就能搞定。
	
	使用如下：
	
	```
	@RxJava
	public String name;
	```
	
	生成方法：
	
	```
    @DefString("")
    public Observable<String> getName1() {
        return Observable.create(
                new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        e.onNext(getName());
                        e.onComplete();
                    }
                }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
	```
	
	使用：
	
	```
    preferenceHandle.getName1().subscribe(new Consumer<String>() {
        @Override
        public void accept(String name) throws Exception {
            Toast.makeText(MainActivity.this, "name = " + name, Toast.LENGTH_SHORT).show();
        }
    });
	```
	
	有人说这生成的是RxJava2啊，我还在用RxJava1咋办？别着急大兄弟，添给注解添加`version`属性配置即可。
	
	生成方法：
	
	```
    @DefString("")
    public Observable<String> getName1() {
        return Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext(getName());
                        subscriber.onCompleted();
                    }
                }
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
	```
	
	有人又说了，你这个`getName1()`感觉不爽，我想换个名字。添加注解属性就可以啦。
	
	```
	@RxJava(suffix = "牛")
	public String name;
	```
	
	那么在使用的时候就是这样的：

	```
	preferenceHandle.getName牛().subscribe(new Consumer<String>() {
		@Override
		public void accept(String name) throws Exception {
			Toast.makeText(MainActivity.this, "name = " + name, Toast.LENGTH_SHORT).show();
		}
	});
	```

## 原理

> 原理比较简单，相信聪明的你早就想到啦。不就是编译时注解搞的鬼嘛，恭喜你答对了。

1. 所有的数据都封装到`BaseModel`中然后转换为JSON存储字符串到`SharePreference`

	```
	final class BaseModel<T> {
	
	    public long createTime;
	    public long updateTime;
	    public long expireTime;
	    public long expire;
	    public T data;
		
		// ... ...
	
	}
	```
	
	通过几个时间字段来标记过期信息。
	
2. 仿照`Retrofit`定义的JSON转换接口

	> 由于大家项目中使用JSON转换工具存在差异，有人喜欢使用`GSON`，有同学觉得`FastJson`是速度最快的，也有同学感觉`Jackson`最优。这里都可以根据自己的情况灵活配置，当然如果使用其他的你也可以自己去写转换器，或者告诉我我去添加支持。

	```
	public interface Converter<F, T> {
	
	    T convert(F value) throws Exception;
	
	    interface Factory {
	        <F> Converter<F, String> fromType(Type fromType);
	
	        <T> Converter<String, T> toType(Type toType);
	    }
	
	}
	```
	
	初始化依旧模仿：
	
	```
	Hannibai.setConverterFactory(GsonConverterFactory.create());
	```
	
3. 生成接口

	1. 首先生成`IHandle`接口
	
		> 只有一个`removeAll()`方法，其实主要是为了混淆好配置而进行的抽取。
	
		```
		public interface IHandle {
			@Apply
			void removeAll();
		}
		```
		
	2. 然后根据`AppPreference`生成`AppPreferenceHandle`接口

		> 这里为对`AppPreference`变量生成操作方法。
		
		```
		public interface AppPreferenceHandle extends IHandle {
		
		  @DefString("zwenkai")
		  boolean containsName();
		
		  @DefString("zwenkai")
		  String getName();
		
		  @Apply
		  void setName(final String name);
		
		  @Apply
		  void removeName();
		}
		```
	
	3. 最后根据`AppPreference`生成`AppPreferenceHandleImpl`类

		> 该类为`AppPreference`接口的实现以及单例模式的封装。
		
	```
	final class AppPreferenceHandleImpl implements AppPreferenceHandle, IHandle {
		private final String mSharedPreferencesName = "com.haha.hannibaitest.AppPreference";
	
		private final String mId;
	
		private AppPreferenceHandleImpl() {
			this.mId = "";
		}
	
		public AppPreferenceHandleImpl(String id) {
			this.mId = id;
		}
	
		public static AppPreferenceHandleImpl getInstance() {
			return Holder.INSTANCE;
		}
	
		@DefString("zwenkai")
		public boolean containsName() {
			return Hannibai.contains1(mSharedPreferencesName, mId, "name", "zwenkai");
		}
	
		@DefString("zwenkai")
		public String getName() {
			return Hannibai.get1(mSharedPreferencesName, mId, "name", "zwenkai");
		}
	
		@Apply
		public void setName(final String name) {
			Hannibai.set1(mSharedPreferencesName, mId, "name", -1L, false, name);
		}
	
		@Apply
		public void removeName() {
			Hannibai.remove1(mSharedPreferencesName, mId, "name");
		}
	
		@Apply
		public void removeAll() {
			Hannibai.clear(mSharedPreferencesName, mId);
		}
	
		private static class Holder {
			private static final AppPreferenceHandleImpl INSTANCE = new AppPreferenceHandleImpl();
		}
	}
	```
	
4. 数据加密

	> 数据加密比较简单，就是对`SharePreference`中`Key`对应的`Value`进行亦或运算。加密解密的方法为同一个，很巧妙，有兴趣的同学可以研究下。
	
	```
	static final String endecode(String input) {
		char[] key = "Hannibai".toCharArray();
		char[] inChars = input.toCharArray();
		for (int i = 0; i < inChars.length; i++) {
			inChars[i] = (char) (inChars[i] ^ key[i % key.length]);
		}
		return new String(inChars);
	}
	```
	
	为什么之前说这样加密可以减小存储文件的大小呢？
	
	**未加密：**
	
	```
	<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
	<map>
		<string name="name">{&quot;data&quot;:&quot;Kevin&quot;,&quot;createTime&quot;:1509687733860,&quot;expire&quot;:-1,&quot;expireTime&quot;:0,&quot;updateTime&quot;:1509946500232}</string>
    </map>
	```
	
	**加密：**
	
	```
	<?xml version='1.0' encoding='utf-8' standalone='yes' ?>
	<map>
	    <string name="name">3C CSj* CEj   =! LSSTYqWVY^QRQ~QBL :LTDSMK-5%LTYNC8 -CT_\RXP|WYV]VPQ5</string>
	</map>
	```
	
	其实主要是把`"`的转义字符`&quot;`给替换掉了，压缩点在这里。
	
5. 具体操作类

	> 在生成的操作类中，可以看到都是调用了`Hannibai`类的方法，那么这里面是怎么封装的呢？
	
	```
	public final class Hannibai {

		static boolean debug = false;

		public static final void init(Context context) {
			RealHannibai.getInstance().init(context, true);
		}

		public static final void init(Context context, boolean encrypt) {
			RealHannibai.getInstance().init(context, encrypt);
		}

		public static final void setDebug(boolean debug) {
			Hannibai.debug = debug;
		}

		public static final <T> T create(final Class<T> preference) {
			return RealHannibai.getInstance().create(preference);
		}

		public static final <T> T create(final Class<T> preference, String id) {
			return RealHannibai.getInstance().create(preference, id);
		}

		public static final void setConverterFactory(Converter.Factory factory) {
			RealHannibai.getInstance().setConverterFactory(factory);
		}

		public static final <T> boolean contains1(String name, String id, String key, T defValue) {
			return RealHannibai.getInstance().contains(name, id, key, defValue.getClass());
		}

		public static final <T> boolean contains2(String name, String id, String key, Type type) {
			return RealHannibai.getInstance().contains(name, id, key, type);
		}

		public static final <T> T get1(String name, String id, String key, T defValue) {
			return RealHannibai.getInstance().get(name, id, key, defValue, defValue.getClass());
		}

		public static final <T> T get2(String name, String id, String key, Type type) {
			return RealHannibai.getInstance().get(name, id, key, null, type);
		}

		public static final <T> void set1(String name, String id, String key, long expire, boolean updateExpire, T newValue) {
			RealHannibai.getInstance().set1(name, id, key, expire, updateExpire, newValue);
		}

		public static final <T> boolean set2(String name, String id, String key, long expire, boolean updateExpire, T newValue) {
			return RealHannibai.getInstance().set2(name, id, key, expire, updateExpire, newValue);
		}

		public static final void remove1(String name, String id, String key) {
			RealHannibai.getInstance().remove1(name, id, key);
		}

		public static final boolean remove2(String name, String id, String key) {
			return RealHannibai.getInstance().remove2(name, id, key);
		}

		public static final void clear(String name, String id) {
			RealHannibai.getInstance().clear(name, id);
		}

	}
	```
	
	好吧，`Hannibai`类可以说啥事没干，大部分工作是对`RealHannibai`的封装。
	
6. 不骗你，真的操作类

	1. 如何获取定义变量操作类的？

		> 通过之前的介绍，定义的`AppPreference`首先生成`AppPreferenceHandle`接口，然后生成`AppPreferenceHandle`接口的实现类`AppPreferenceHandleImpl`。
		
		在使用`AppPreference`的时候是这样的：
		
		```
		AppPreferenceHandle preferenceHandle = Hannibai.create(AppPreferenceHandle.class);
		```
		
		那是怎么通过接口获取的实现类呢？在`RealHannibai`中：
		
		```
		final public <T> T create(final Class<T> preference) {
			Utils.validateHandleInterface(preference);
			try {
				return (T) Class.forName(preference.getName() + "Impl")
						.getMethod("getInstance")
						.invoke(null);
				} catch (Exception e) {
					Log.e(TAG, "Something went wrong!");
					throw new RuntimeException(e);
        	}
    	}
		```
		
		跟简单，就是根据`AppPreferenceHandle`拼接`Impl`找到`AppPreferenceHandleImpl`类，然后反射调用它的`getInstance()`静态方法。
		
		获取带`id`的实现类：
		
		```
		final public <T> T create(final Class<T> preference, String id) {
			Utils.validateHandleInterface(preference);
			try {
				return (T) Class.forName(preference.getName() + "Impl")
                    .getConstructor(String.class)
                    .newInstance(id);
			} catch (Exception e) {
				Log.e(TAG, "Something went wrong!");
            	throw new RuntimeException(e);
        	}
    	}
		```
		
		这个也是拼接到类名，然后反射它的构造方法获取实例。
		
	2. 判断是否包含`Key`

		> 首先获取`Key`对应的`Value`，如果`Value`为空则不包含`Key`，如果`Value`不为空则将数据转换为BaseModel实体看是否过期，过期也为不包含，不过期则为包含该`Key`。

		```
		final <T> boolean contains(String name, String id, String key, Type type) {
		    String value = getSharedPreferences(name, id).getString(key, null);
		    if (value == null || value.length() == 0) {
		        if (Hannibai.debug)
		            Log.d(TAG, String.format("Value of %s is empty.", key));
		        return false;
		    } else {
		        ParameterizedType parameterizedType = type(BaseModel.class, type);
		        BaseModel<T> model = null;
		        try {
		            model = (BaseModel<T>) getConverterFactory().toType(parameterizedType).convert(mEncrypt ? Utils.endecode(value) : value);
		        } catch (Exception e) {
		            if (mEncrypt) {
		                Log.e(TAG, "Convert JSON to Model failed，will use unencrypted retry again.");
		            } else {
		                Log.e(TAG, "Convert JSON to Model failed，will use encrypted retry again.");
		            }
		            if (Hannibai.debug) {
		                e.printStackTrace();
		            }
		            try {
		                model = (BaseModel<T>) getConverterFactory().toType(type).convert(mEncrypt ? value : Utils.endecode(value));
		            } catch (Exception e1) {
		                Log.e(TAG, "Convert JSON to Model complete failure.");
		                if (Hannibai.debug) {
		                    e1.printStackTrace();
		                }
		            }
		        }
		
		        if (null == model) {
		            return false;
		        }
		
		        if (model.dataExpired()) {
		            if (Hannibai.debug)
		                Log.d(TAG, String.format("Value of %s is %s expired, return false.", key, model.data));
		            return false;
		        } else {
		            return true;
		        }
		    }
		}
		```
		
		这里进行了解密的尝试，比如之前配置的为加密，存储了数据，然后又配置了未加密，这时按照配置读取是错误的，配置说未加密实际上是加密的，这里进行了容错处理。
		
	3. 获取`Key`对应`Value`

		> 首先获取`Key`对应的`Value`，如果`Value`为空则返回默认值，如果`Value`不为空则将数据转换为BaseModel实体看是否过期，过期也返回默认值，不过期则返回`BaseModel`中对应数据。

		```
	    final <T> T get(String name, String id, String key, T defValue, Type type) {
	        if (Hannibai.debug) Log.d(TAG, String.format("Retrieve the %s from the preferences.", key));
	        String value = getSharedPreferences(name, id).getString(key, null);
	        if (value == null || value.length() == 0) {
	            if (Hannibai.debug)
	                Log.d(TAG, String.format("Value of %s is empty, return the default %s.", key, defValue));
	            return defValue;
	        } else {
	            ParameterizedType parameterizedType = type(BaseModel.class, type);
	            BaseModel<T> model = null;
	            try {
	                model = (BaseModel<T>) getConverterFactory().toType(parameterizedType).convert(mEncrypt ? Utils.endecode(value) : value);
	            } catch (Exception e) {
	                if (mEncrypt) {
	                    Log.e(TAG, "Convert JSON to Model failed，will use unencrypted retry again.");
	                } else {
	                    Log.e(TAG, "Convert JSON to Model failed，will use encrypted retry again.");
	                }
	                if (Hannibai.debug) {
	                    e.printStackTrace();
	                }
	                try {
	                    model = (BaseModel<T>) getConverterFactory().toType(type).convert(mEncrypt ? value : Utils.endecode(value));
	                } catch (Exception e1) {
	                    Log.e(TAG, String.format("Convert JSON to Model complete failure, will return the default %s.", defValue));
	                    if (Hannibai.debug) {
	                        e1.printStackTrace();
	                    }
	                }
	            }
	
	            if (null == model) {
	                return defValue;
	            }
	
	            if (Hannibai.debug) {
	                Log.d(TAG, String.format("Value of %s is %s, create at %s, update at %s.", key, model.data, model.createTime, model.updateTime));
	                if (!model.dataExpired()) {
	                    if (model.expire > 0) {
	                        Log.d(TAG, String.format("Value of %s is %s, Will expire after %s seconds.", key, model.data, (model.expireTime - System.currentTimeMillis()) / 1000));
	                    } else {
	                        Log.d(TAG, String.format("Value of %s is %s.", key, model.data));
	                    }
	
	                }
	            }
	            if (model.dataExpired()) {
	                if (Hannibai.debug)
	                    Log.d(TAG, String.format("Value of %s is %s expired, return the default %s.", key, model.data, defValue));
	                return defValue;
	            } else {
	                return model.data;
	            }
	        }
	    }
		```
		
	4. 设置`Key`对应值

		> 首先获取`Key`对应的`Value`，如果`Value`不为空，转换为`BaseModel`实体，更新对应数据，过期时间信息，然后转化为JSON字符串存储，如果`Value`为空则创建`BaseModel`并转化为JSON字符串存储。

		```
		private final <T> SharedPreferences.Editor set(String name, String id, String key, long expire, boolean updateExpire, T newValue) throws Exception {
	        if (Hannibai.debug) Log.d(TAG, String.format("Set the %s value to the preferences.", key));
	        BaseModel<T> model = null;
	        ParameterizedType type = type(BaseModel.class, newValue.getClass());
	        SharedPreferences sharedPreferences = getSharedPreferences(name, id);
	        String value = sharedPreferences.getString(key, null);
	        if (value != null && value.length() != 0) {
	            try {
	                model = (BaseModel<T>) getConverterFactory().toType(type).convert(mEncrypt ? Utils.endecode(value) : value);
	            } catch (Exception e) {
	                if (mEncrypt) {
	                    Log.e(TAG, "Convert JSON to Model failed，will use unencrypted retry again.");
	                } else {
	                    Log.e(TAG, "Convert JSON to Model failed，will use encrypted retry again.");
	                }
	                if (Hannibai.debug) {
	                    e.printStackTrace();
	                }
	                try {
	                    model = (BaseModel<T>) getConverterFactory().toType(type).convert(mEncrypt ? value : Utils.endecode(value));
	                } catch (Exception e1) {
	                    Log.e(TAG, "Convert JSON to Model complete failure.");
	                    if (Hannibai.debug) {
	                        e1.printStackTrace();
	                    }
	                }
	            }
	            if (null == model) {
	                model = new BaseModel<>(newValue, expire);
	            } else {
	                if (model.dataExpired()) {
	                    model = new BaseModel<>(newValue, expire);
	                    if (Hannibai.debug)
	                        Log.d(TAG, String.format("Value of %s is %s expired", key, model.data));
	                } else {
	                    model.update(newValue, updateExpire);
	                }
	            }
	        } else {
	            model = new BaseModel<>(newValue, expire);
	        }
	        String modelJson = getConverterFactory().fromType(type).convert(model);
	        return sharedPreferences.edit().putString(key, mEncrypt ? Utils.endecode(modelJson) : modelJson);
	    }
		```
	
# 混淆

> 如果使用了混淆，添加如下到混淆配置文件

```
-dontwarn com.kevin.hannibai.**
-keep class com.kevin.hannibai.** { *; }
-keep class * implements com.kevin.hannibai.IHandle { *; }
-keep @com.kevin.hannibai.annotation.SharePreference class * { *; }
```