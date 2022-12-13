# python-SQL交互文档
## sql结构和查找规范

**12.13更新：修改和查找增加了并发事务，加入了残缺的触发器和视图**

### register_info:
* reg_id: INT , 记录每一个用户的id（唯一非空）
* reg_name: CHAR, 记录用户名，非空唯一，长度限制为45字符（需要提前检查）
* post_history_id: INT, 目前没用
* usr_pswd: 用户密码
* date: 用户账号创建日期
以下均为个人信息，可以存在，也可以不存在为null
* usr_gender: 性别
* usr_email: email
* usr_phone_num: 电话号码
* usr_age: 年龄
* usr_school: 学校
* usr_photo_location: 照片地址
* usr_nickname: 昵称

==new:==

* update_trigger:触发器，用来更新的时候进行插入操作

有关的操作:
```python
def check_pswd(usr_name,pswd):
```
bool函数，输出为密码是否匹配，且账号是否存在
```python
def get_pswd_from_name(name):
```
拿到密码，可以用作debug
```python
def get_id_from_name(name):
```
通过name获得id，其余所有函数都是用id作为唯一标识符
```python
def register_usr_name_and_pswd(usr_name, pswd):
```
如果被占用返回false，否则注册并返回true
```python
def update_usr_info(usr_name, usr_gender, usr_phone_num, usr_photo_location, 
                    usr_nickname, usr_school, usr_age, usr_email):
```
usr_name必须，其他无所谓，但是usr_name必须合法，在调用后更新当前usr_name的相关信息
```python
def get_usr_info_by_usr_name(usr_name):
```
查找usr_name下的个人信息，需要验证是否具有权限。返回为json样式字典，格式为：
```python
res = cursor.fetchall()[0]
json_res = {
    "id": res[0],
    "name": res[1],
    "post_history_id": res[2],
    "pswd": res[3],
    "date": res[4],
    "gender": res[5],
    "email": res[6],
    "phone_num": res[7],
    "age": res[8],
    "school": res[9],
    "photo_location": res[10],
    "nickname": res[11],
}
```

### posts_info:
* post_id:INT，唯一标识符，用来定位帖子
* usr_id:INT，用来定位帖子的作者
* next_id:INT，用来寻找帖子的下一个回帖（爬楼用）
* state:帖子状态，暂时用不到
* topic:帖子题目，也可以没有题目
* content:帖子内容，用来记录帖子的主体部分
* comment_id:如果帖子是回帖，记录被回的帖子id，否则为-1
* type:帖子的主题，暂时用不到
* date:发帖时间
* comment_num:最近数量
* total_comment_num:全部数量

相关的操作：
```python
def get_post_ids_from_id(usr_id):
```
通过个人id查找所有发过的帖子id，返回是tuple
```python
def get_post_content_from_post_id(post_id):
```
查找一个post_id对应的内容，用来简略的显示一个帖子的一部分

```python
def get_reference_id_from_post_id(post_id):
```
找到当前post_id对应引用的id，如果是帖子而非回复，则返回值等于他自己的id
```python
def get_father_id_from_post_id(post_id):
```
找到根id，即每一个帖子的主帖
```python
def get_full_post_from_post_id(post_id):
```
当前post_id对应的主帖，找到全部的楼，返回值为一个字典，它具有：
```python
{
    主帖内容,
    回帖序号:{
        回帖内容，
        回帖序号，
        被回帖内容，
        被回帖序号，
    },
}
content[tall]["content"] = get_post_content_from_post_id(next_id[0])
content[tall]["id"] = next_id[0]
content[tall]["reference_id"] = get_reference_id_from_post_id(next_id[0])
content[tall]["reference_content"] = get_post_content_from_post_id(content[tall]["reference_id"])
```
的json结构
```python
def get_last_post_id_from_post_id(post_id):
```
当前post_id对应的主帖，找到最后一栋楼的id
```python
def add_new_post(usr_id, content, type):
```
添加一个新帖，同时记录发帖时间==new:==同时修改了函数参数和一些bug，并加入了版面id
```python
def add_new_comment(post_id, usr_id, content, reference_id):
```
添加一个新的帖子回复，与添加新帖不同，需要给出被回复帖子的id。==new:==版面默认为0
```python
def get_top_ten_posts():
```
查找10大，方法是按照最近一天的comment数量评判（更新方法还没实现）

==new:==

```python
def get_post_from_type(type):
```

输入typeid，返回所有符合的帖子

### likes:
* id: INT，唯一标识符，没有作用
* usr_id: 这一个喜欢对象的usr_id
* post_id: 这一个喜欢对象的post_id
* opinion: 喜欢还是不喜欢（1代表喜欢，-1代表不喜欢）

对应方法：
```python
def like(usr_id, likes, post_id):
```
更新，usrid post_id分别代表用户id和帖子id，like为点赞还是点踩。如果无表项，则添加。如果
表项相同则删除表项（对应同一个赞，踩点击两次），如果不同则更改

```python
def get_like_numbers(post_id):
```
对一个帖子统计赞踩总数，返回为一个tuple，第一项为赞总数，第二项为踩总数

### stars:
* id: INT，唯一标识符，没有作用
* usr_id: 这一个收藏对象的usr_id
* post_id: 这一个收藏对象的post_id

对应方法：
```python
def star(usr_id, post_id):
```
更新，根据usr_id和post_id建立收藏项，如果已经存在则删除

```python
def get_stars_from_usr_id(usr_id):
```
通过usr_id来找寻收藏夹，返回值为tuple，每一项代表一个收藏的项目

### others

==new:==

init函数：暂定用来更新和定义触发器和视图，具体实现还没搞清楚

