# ChangeHost 

动态修改Host地址

# 使用方法

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ServiceLocation.DEBUG = false;
    ServiceLocation.startFloat(this, "http://www.baidu.com");
    this.initView();
}
```

```java
@Override
protected void onDestroy() {
    ServiceLocation.stopFloat(this);
    super.onDestroy();
}
```

# 注意事项

因为定制版ROM，有些系统限制了系统弹窗，所以不能使用本功能。

有关修复这个问题的解决方案正在实现中。