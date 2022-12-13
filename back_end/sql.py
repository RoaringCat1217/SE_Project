import pymysql
import time
from  bbs.config import Config


def init():
    """
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "create view type_posts as select post_id from posts_info "
    cursor.execute(sql)
    sql1 = "delimiter // "
    sql2 = "create trigger insert_usr after insert on register_info for each row begin " + \
        "declare a INT default 0;set a = (select max(id) from usr_logs) + 1;" + \
        "insert into usr_logs(`id`, `time`, `content_sql`, state) VALUES("+\
         "a " + ",\"" + \
        str(time.localtime().tm_year) + "/" + str(time.localtime().tm_mon) + "/" + str(time.localtime().tm_wday) + \
        " " + str(time.localtime().tm_hour) + ":" + str(time.localtime().tm_min) + ":" + str(time.localtime().tm_sec) + \
         "\", \"insert\", \"success\"" + "); end //"
    print(sql2)
    cursor.execute(sql1,sql2)
    sql1 = "delimiter // "
    sql2= "delimiter // " + "create trigger update_usr after update on register_info for each row begin " + \
        "declare a INT default 0;set a = (select max(id) from usr_logs) + 1;" + \
        "insert into usr_logs(`id`, `time`, `content_sql`, state) VALUES("+\
         "a " + ",\"" + \
        str(time.localtime().tm_year) + "/" + str(time.localtime().tm_mon) + "/" + str(time.localtime().tm_wday) + \
        " " + str(time.localtime().tm_hour) + ":" + str(time.localtime().tm_min) + ":" + str(time.localtime().tm_sec) + \
         "\", \"update\", \"success\"" + ");  end //"
    print(sql2)
    cursor.execute(sql1,sql2)

    sql = "delimiter ; "
    cursor.execute(sql)
    """


def crash():
    """
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "drop view type_posts"
    cursor.execute(sql)
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "drop trigger insert_usr"
    print(sql)
    cursor.execute(sql)
    sql = "drop trigger update_usr"
    print(sql)
    cursor.execute(sql)
    """


# authenticate


def check_pswd(usr_name,pswd):
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select reg_id from register_info where reg_name = \"" + usr_name + "\""
    cursor.execute(sql)
    res = cursor.fetchone()
    if res == None:
        return False
    usr_pswd = get_pswd_from_name(usr_name)
    if pswd == usr_pswd:
        return True
    return False


def get_pswd_from_name(name):
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select usr_pswd from register_info where reg_name = \"" + name + "\""
    cursor.execute(sql)
    res = cursor.fetchone()
    conn.close()
    return res


def get_id_from_name(name):
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select reg_id from register_info where reg_name = \"" + name + "\""
    cursor.execute(sql)
    res = cursor.fetchone()
    conn.close()
    if not res:
        return None
    return res[0]


# posts

def get_post_ids_from_id(usr_id):
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select post_id from posts_info where usr_id = \"" + str(usr_id) + "\""
    cursor.execute(sql)
    res = cursor.fetchall()
    conn.close()
    return res


def get_post_content_from_post_id(post_id):
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select * from posts_info where post_id = \"" + str(post_id) + "\""
    cursor.execute(sql)
    res = cursor.fetchone()
    conn.close()
    return res


def get_reference_id_from_post_id(post_id):
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select comment_id from posts_info where post_id = \"" + str(post_id) + "\""
    cursor.execute(sql)
    res = cursor.fetchone()[0]
    conn.close()
    return res


def get_father_id_from_post_id(post_id):
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select comment_id from posts_info where post_id = \"" + str(post_id) + "\""
    cursor.execute(sql)
    cur_post_id = post_id
    res = cursor.fetchone()[0]
    while int(res) != -1:
        sql = "select comment_id from posts_info where post_id = \"" + str(res) + "\""
        cursor.execute(sql)
        cur_post_id = res
        res = cursor.fetchone()[0]
    conn.close()
    return cur_post_id


def get_full_post_from_post_id(post_id):
    conn = pymysql.connect( host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select next_id from posts_info where post_id = \"" + str(post_id) + "\""
    cursor.execute(sql)
    content = {}
    tall = 0
    content[tall] = get_post_content_from_post_id(post_id)
    tall += 1
    next_id = cursor.fetchone()
    while next_id[0] != -1:
        content[tall] = {}
        sql = "select next_id from posts_info where post_id = \"" + str(next_id[0]) + "\""
        cursor.execute(sql)
        content_all = cursor.fetchone()
        content[tall]["full"] = content_all
        content[tall]["content"] = get_post_content_from_post_id(next_id[0])
        content[tall]["id"] = next_id[0]
        content[tall]["reference_id"] = get_reference_id_from_post_id(next_id[0])
        content[tall]["reference_content"] = get_post_content_from_post_id(content[tall]["reference_id"])
        tall += 1
        sql = "select next_id from posts_info where post_id = \"" + str(next_id[0]) + "\""
        cursor.execute(sql)
        next_id = cursor.fetchone()

    conn.close()
    return content


def get_last_post_id_from_post_id(post_id):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select next_id from posts_info where post_id = \"" + str(post_id) + "\""
    cursor.execute(sql)
    cur_post_id = post_id
    res = cursor.fetchone()[0]
    while int(res) != -1:
        sql = "select next_id from posts_info where post_id = \"" + str(res) + "\""
        cursor.execute(sql)
        cur_post_id = res
        res = cursor.fetchone()[0]
    conn.close()
    return cur_post_id


def get_top_ten_posts():
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select * from posts_info where comment_id = -1 and comment_num >= 5 order by comment_num desc limit 10;"
    cursor.execute(sql)
    res = cursor.fetchall()
    conn.close()
    return res


def get_post_from_type(type):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    # @TODO finish view
    sql = "select * from posts_info where type = " + str(type)
    cursor.execute(sql)
    res = cursor.fetchall()
    return res



# add 部分


def add_new_post(usr_id, topic, content, type):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                            , autocommit=True)
    cursor = conn.cursor()
    ok = 0
    sql = "start transaction"
    cursor.execute(sql)
    try:
        sql = "select MAX(post_id) from posts_info"
        cursor.execute(sql)
        last_id = cursor.fetchone()[0]
        if last_id == None:
            last_id = -1
        last_id += 1
        sql = "insert into posts_info (`post_id`, `usr_id`, `next_id`, `content`, `comment_id`, `date`, `comment_num`, `total_comment_num`, `type`, `topic`) VALUES (" + \
            str(last_id) + "," + str(usr_id) + "," + str(-1) + ",\"" + str(content) + "\"," + str(-1) + ",\"" + \
            str(time.localtime().tm_year) + "/" + str(time.localtime().tm_mon) + "/" + str(time.localtime().tm_wday) + \
            " " + str(time.localtime().tm_hour) + ":" + str(time.localtime().tm_min) + ":" + str(time.localtime().tm_sec) + \
            "\"," + str(0) +"," + str(0) + "," + str(type) + ',\"' + topic + "\")"

        cursor.execute(sql)
        return
    except Exception as e :
        ok = 1
    finally:
        if not ok:
            sql = "commit"
            cursor.execute(sql)
            conn.close()
        else:
            sql = "rollback"
            cursor.execute(sql)
            conn.close()

def add_new_comment(post_id, usr_id, content, reference_id):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                            , autocommit=True)
    cursor = conn.cursor()
    ok = 0
    sql = "start transaction"
    cursor.execute(sql)
    try:
        sql = "select MAX(post_id) from posts_info"
        cursor.execute(sql)
        last_id = cursor.fetchone()[0] + 1
        this_last_id = get_last_post_id_from_post_id(post_id)
        sql = "update posts_info set next_id = " + str(last_id) + " where post_id = " + str(this_last_id)
        cursor.execute(sql)
        print("insert success1")
        sql = "insert into posts_info (`post_id`, `usr_id`, `next_id`, `content`, `comment_id`, `date`, `comment_num`, `total_comment_num`, `type`) VALUES (" + \
            str(last_id ) + "," + str(usr_id) + "," + str(-1) + ",\"" + str(content) + "\"," + str(reference_id) + ",\"" + \
            str(time.localtime().tm_year) + "/" + str(time.localtime().tm_mon) + "/" + str(time.localtime().tm_mday) + \
            " " + str(time.localtime().tm_hour) + ":" + str(time.localtime().tm_min) + ":" + str(time.localtime().tm_sec) + \
            "\"," + str(0) + "," + str(0) + "," + 'null' + ")"
        cursor.execute(sql)
        print("insert success2")
        father_id = get_father_id_from_post_id(this_last_id)
        conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
        sql = "select comment_num from posts_info where post_id = " + str(father_id)
        cursor.execute(sql)
        res = cursor.fetchone()[0]
        sql = "update posts_info set comment_num = " + str(res+1) + " where post_id = " + str(father_id)
        print("sql is: ",sql)
        cursor.execute(sql)
        sql = "select total_comment_num from posts_info where post_id = " + str(father_id)
        cursor.execute(sql)
        res = cursor.fetchone()[0]
        sql = "update posts_info set total_comment_num = " + str(res+1) + " where post_id = " + str(father_id)
        cursor.execute(sql)
        return
    except Exception as e :
        ok = 1
    finally:
        if not ok:
            sql = "commit"
            cursor.execute(sql)
            conn.close()
        else:
            sql = "rollback"
            cursor.execute(sql)
            conn.close()


def register_usr_name_and_pswd(usr_name, pswd):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                            , autocommit=True)
    cursor = conn.cursor()
    ok = 0
    sql = "start transaction"
    cursor.execute(sql)
    try:
        sql = "select reg_id from register_info where reg_name = \"" + usr_name + "\""
        cursor.execute(sql)
        res = cursor.fetchone()
        print(res)
        if res != None:
            return False
        sql = "select MAX(reg_id) from register_info"
        cursor.execute(sql)
        max_id = cursor.fetchone()[0]
        print(f'max_id = {max_id}')
        if max_id == None:
            max_id = -1
        sql = "insert into register_info (`reg_id`, `reg_name`, `usr_pswd`, `date`) VALUES (" + \
            str(max_id+1) + ",\"" + str(usr_name) + "\"," + '"' + str(pswd)  + '"' + ",\"" + \
            str(time.localtime().tm_year) + "/" + str(time.localtime().tm_mon) + "/" + str(time.localtime().tm_mday) + \
            " " + str(time.localtime().tm_hour) + ":" + str(time.localtime().tm_min) + ":" + str(time.localtime().tm_sec) + "\")"
        cursor.execute(sql)
        return True
    except Exception as e :
        ok = 1
    finally:
        if not ok:
            sql = "commit"
            cursor.execute(sql)
            conn.close()
        else:
            sql = "rollback"
            cursor.execute(sql)
            conn.close()

def update_usr_info(usr_name, usr_gender, usr_phone_num, usr_photo_location, usr_nickname, usr_school, usr_age, usr_email):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                           , autocommit=True)
    cursor = conn.cursor()
    ok = 0
    sql = "start transaction"
    cursor.execute(sql)
    try:
        if usr_gender:
            sql = "update register_info set usr_gender = \"" + str(usr_gender) + "\" where reg_name = \"" +str(usr_name)+ "\""
            cursor.execute(sql)
        if usr_phone_num:
            sql = "update register_info set usr_phone_num = \"" + str(usr_phone_num) + "\" where reg_name = \"" + str(usr_name)+ "\""
            cursor.execute(sql)
        if usr_photo_location:
            sql = "update register_info set usr_photo_location = \"" + str(usr_photo_location) + "\" where reg_name = \"" + str(
                usr_name) + "\""
            cursor.execute(sql)
        if usr_nickname:
            sql = "update register_info set usr_nickname = \"" + str(usr_nickname) + "\" where reg_name = \"" + str(
                usr_name) + "\""
            cursor.execute(sql)
        if usr_school:
            sql = "update register_info set usr_school = \"" + str(usr_school) + "\" where reg_name = \"" + str(
                usr_name) + "\""
            cursor.execute(sql)
        if usr_age:
            sql = "update register_info set usr_age = \"" + str(usr_age) + "\" where reg_name = \"" + str(
                usr_name) + "\""
            cursor.execute(sql)
        if usr_email:
            sql = "update register_info set usr_email = \"" + str(usr_email) + "\" where reg_name = \"" + str(
                usr_name) + "\""
            cursor.execute(sql)
    except Exception as e :
        ok = 1
    finally:
        if not ok:
            sql = "commit"
            cursor.execute(sql)
            conn.close()
        else:
            sql = "rollback"
            cursor.execute(sql)
            conn.close()


def get_usr_info_by_id(usr_id):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                           , autocommit=True)
    cursor = conn.cursor()
    sql = "select * from register_info where reg_id = \"" + str(usr_id) + "\""
    cursor.execute(sql)
    res = cursor.fetchall()
    if res:
        res = res[0]
    else:
        conn.close()
        return None
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
    conn.close()
    return json_res


def change_usr_name(usr_name, new_usr_name):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                            , autocommit=True)
    cursor = conn.cursor()
    ok = 0
    sql = "start transaction"
    cursor.execute(sql)
    try:
        sql = "select reg_id from register_info where reg_name = \"" + new_usr_name + "\""
        cursor.execute(sql)
        res = cursor.fetchone()
        print(res)
        if res != None:
            return False
        sql = "update register_info set reg_name = \"" + str(new_usr_name) + "\" where reg_name = \"" + str(usr_name) + "\""
        cursor.execute(sql)
        return True
    except Exception as e :
        ok = 1
    finally:
        if not ok:
            sql = "commit"
            cursor.execute(sql)
            conn.close()
        else:
            sql = "rollback"
            cursor.execute(sql)
            conn.close()


# likes: 1, -1分别代表喜欢和不喜欢
def like(usr_id, likes, post_id):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                            , autocommit=True)
    cursor = conn.cursor()
    sql = "select MAX(id) from likes"
    cursor.execute(sql)
    new_id = cursor.fetchone()[0]
    if new_id != None:
        new_id = new_id + 1
    else:
        new_id = 0
    cursor = conn.cursor()
    ok = 0
    sql = "start transaction"
    cursor.execute(sql)
    sql = "select opinion from likes where usr_id = " + str(usr_id) + " and post_id = " + str(post_id)
    cursor.execute(sql)
    res = cursor.fetchone()
    try:
        if res and res[0] == likes:
            sql = "delete from likes  where usr_id = " + str(usr_id) + " and post_id = " + str(post_id)
            print(sql)
            cursor.execute(sql)
        elif not res:
            sql = "insert into likes (`id`, `usr_id`, `post_id`, `opinion`) VALUES(" + str(new_id) + "," + str(usr_id) + "," + \
                  str(post_id) + "," + str(likes) + ")"
            print(sql)
            cursor.execute(sql)
        else:
            sql = "update likes set opinion = " + str(likes) + " where usr_id = " + str(usr_id) + " and post_id = " + str(post_id)
            print(sql)
            cursor.execute(sql)
    except Exception as e :
        ok = 1
    finally:
        if not ok:
            sql = "commit"
            cursor.execute(sql)
        else:
            sql = "rollback"
            cursor.execute(sql)




    conn.close()
    return


# return likes and dislikes
def get_like_numbers(post_id):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                            , autocommit=True)
    cursor = conn.cursor()
    sql = "select SUM(opinion) from likes where opinion = 1 and post_id = " + str(post_id)
    cursor.execute(sql)
    res1 = cursor.fetchone()[0]
    if not res1:
        res1 = 0
    sql = "select SUM(opinion) from likes where opinion = -1 and post_id = " + str(post_id)
    cursor.execute(sql)
    res2 = cursor.fetchone()[0]
    if not res2:
        res2 = 0

    conn.close()
    return res1, -res2


# star: 1 refers to star
def star(usr_id, post_id):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                            , autocommit=True)
    cursor = conn.cursor()
    sql = "select MAX(star_id) from collections"
    cursor.execute(sql)
    new_id = cursor.fetchone()[0]
    if new_id != None:
        new_id = new_id + 1
    else:
        new_id = 0
    cursor = conn.cursor()
    sql = "select * from collections where usr_id = " + str(usr_id) + " and post_id = " + str(post_id)
    cursor.execute(sql)
    res = cursor.fetchone()
    ok = 0
    sql = "start transaction"
    cursor.execute(sql)
    try:
        if res:
            sql = "delete from collections  where usr_id = " + str(usr_id) + " and post_id = " + str(post_id)
            cursor.execute(sql)
        elif not res:
            sql = "insert into collections (`star_id`, `usr_id`, `post_id`) VALUES(" + str(new_id) + "," + str(usr_id) + "," +\
                  str(post_id) + ")"
            print(sql)
            cursor.execute(sql)
    except Exception as e :
        ok = 1
    finally:
        if not ok:
            sql = "commit"
            cursor.execute(sql)
        else:
            sql = "rollback"
            cursor.execute(sql)

    conn.close()
    return


# return likes and dislikes
def get_stars_from_usr_id(usr_id):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                            , autocommit=True)
    cursor = conn.cursor()
    sql = "select post_id from collections where usr_id = " + str(usr_id)
    cursor.execute(sql)
    res = cursor.fetchall()

    conn.close()
    return res

def get_types():
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select kind_id,kind_name from kinds"
    cursor.execute(sql)
    res = cursor.fetchall()
    return res

def get_current_ten_posts():
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8')
    cursor = conn.cursor()
    sql = "select * from posts_info where comment_id = -1 and comment_num >= 0 order by date desc limit 10;"
    cursor.execute(sql)
    res = cursor.fetchall()
    conn.close()
    return res

def get_like(usr_id, post_id):
    conn = pymysql.connect(host=Config.mysql_host, port=Config.mysql_port, user=Config.mysql_user, password=Config.mysql_password, database=Config.mysql_database, charset='utf8'
                            , autocommit=True)
    cursor = conn.cursor()
    sql = "select SUM(opinion) from likes where opinion = 1 and post_id = " + str(post_id) + " and usr_id = " + str(usr_id)
    cursor.execute(sql)
    res1 = cursor.fetchone()[0]
    if not res1:
        res1 = 0
    sql = "select SUM(opinion) from likes where opinion = -1 and post_id = " + str(post_id) + " and usr_id = " + str(usr_id)
    cursor.execute(sql)
    res2 = cursor.fetchone()[0]
    if not res2:
        res2 = 0

    conn.close()
    return res1, -res2


# test info
# @TODO search api
if __name__ == '__main__':
    crash()
    init()
    a = get_pswd_from_name("admintest")
    b = get_id_from_name("admintest")
    c = get_post_ids_from_id(0)
    d = get_post_content_from_post_id(1)
    e = get_father_id_from_post_id(9)
    f = get_full_post_from_post_id(1)
    add_new_comment(1, 3, "我爱你", 17)
    add_new_post(3, "我爱你", 1)
    # g = get_post_content_from_post_id(18)
    h = register_usr_name_and_pswd("123","4565788")
    i = change_usr_name("123","12345")
    j = get_top_ten_posts()
    like(3, 1, 1)
    print(get_like_numbers(1)[0], get_like_numbers(1)[1])
    star(3, 1)
    print(get_stars_from_usr_id(3))
    update_usr_info("admintest","male","123","./","hello","bye",1,"hello")
    # print(get_usr_info_by_usr_name("admintest"))
    g = get_post_from_type(0)
    print(g)