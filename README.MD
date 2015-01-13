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
