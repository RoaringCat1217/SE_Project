from flask import Flask,render_template, request,redirect,make_response,jsonify
import os
from sql import *
import numpy as np

#版面目录
catalogues=['跳蚤市场','美食天地','资料共享']

app = Flask(__name__)

@app.route('/login', methods=["GET"])
def login():
    username=request.args.get("username")
    password=request.args.get("password")
    login_res = check_pswd(username, password);
    print(login_res)
    if login_res:
        return jsonify({'code':1})
    else:
        return jsonify({'code':2})

@app.route('/register', methods=["GET"])
def register():
    username=request.args.get("username")
    password=request.args.get("password")
    try:
        register_res = register_usr_name_and_pswd(username, password);
        print(register_res)
        if register_res:
            return jsonify({'code':1})
        else:
            return jsonify({'code':3})
    except :
        return jsonify({'code':2})

@app.route('/hot', methods=["GET"])
def hot():
    
    hotposts=get_top_ten_posts()
    ret={'code':1, 'number':len(hotposts), 'entry':[]}
    k=1
    for post in hotposts:
        ret['entry'].append({'rank': k, 'post_id':post[0],'title': post[4],'content': post[5], 'hot_index': post[10]+10, 'group_name':catalogues[post[7]]})
        k=k+1
    return jsonify(ret)


@app.route('/like', methods=["GET"])
def liked():
    userid=get_id_from_name(request.args.get("username"))
    if userid is None: return jsonify({'code':0})
    postid=request.args.get("post_id")
    like(userid, 1, postid)
    likes,_=get_like_numbers(postid)
    return jsonify({'code':1,'likes':likes})


@app.route('/catalogue', methods=["GET"])
def catalogue():
    return jsonify({'code':1,'groups':catalogues})



@app.route('/group', methods=["GET"])
def group():
    group_name=request.args.get("group_name")
    try:
        type=catalogues.index(group_name)
    except:
        return jsonify({'code':2,'wrong info':f'have no group {group_name} in database'})
    posts=get_post_from_type(type)
    ret_post=[]
    k=1
    #还未按照时间排序
    for post in posts:
        tmp={}
        if post[6]==-1:
            tmp['rank']=k
            tmp['post_id']=post[0]
            tmp['title']=post[4]
            tmp['content']=post[5]
            tmp['poster_name']=get_usr_info_by_id(post[1])['name']
            ret_post.append(tmp)
            k=k+1
    return jsonify({'code':1,'number':len(ret_post),'entry':ret_post})

@app.route('/getpost', methods=["GET"])
def getpost():
    user_name=request.args.get("username")
    post_id=request.args.get("post_id")
    ret_post={}
    
    post=get_post_content_from_post_id(post_id)
    if post is None:
        return jsonify({'code':2,'info':'no this post'})
    #print(post)
    like_num=get_like_numbers(post_id)    
    user_id=get_id_from_name(user_name)
    if user_id is None:
        return jsonify({'code':3,'info':'no this user'})
    poster_info=get_usr_info_by_id(int(post[1]))
    user_stard=[tmp[0] for tmp in get_stars_from_usr_id(user_id)]
    ret_post['code']=1
    ret_post['title']=post[4]
    ret_post['content']=post[5]
    ret_post['likes']=like_num[0]-like_num[1]
    ret_post['liked']=get_like(user_id,post_id)[0]
    ret_post['poster_name']=poster_info["name"]
    ret_post['post_id']=post_id
    if int(post_id) in user_stard:
        ret_post['stared']=1
    else : 
        ret_post['stared']=0
    ret_post['time']=post[8]
    return jsonify(ret_post)


@app.route('/star', methods=["GET"])
def star_():
    user_name=request.args.get("username")
    post_id=request.args.get("post_id")
    userid=get_id_from_name(user_name)
    if userid is None: return jsonify({'code':2})
    star(userid,post_id)
    return jsonify({'code':1})


@app.route('/getstars', methods=["GET"])
def getstars():
    user_name=request.args.get("username")
    userid=get_id_from_name(user_name)
    star_list = get_stars_from_usr_id(userid)
    entrys=[]
    k=1
    for postid in star_list:
        tmp={}
        post=get_post_content_from_post_id(postid[0])
        tmp['rank']=k
        tmp['post_id']=postid[0]
        tmp['title']=post[4]
        tmp['group_name']=catalogues[int(post[7])]
        tmp['content']=post[5]
        tmp['time']=post[8]
        k=k+1
        entrys.append(tmp)
    return jsonify({'code':1, 'entry':entrys})


@app.route('/getreply', methods=["GET"])
def getreply():
    post_id=request.args.get("post_id")
    try:
        reply_list = get_full_post_from_post_id(post_id)
        return jsonify({'code':1,'entry':reply_list})
    except:
        return jsonify({'code':2})

def save_to_file(file_name, contents):
    fh = open(file_name, 'w')
    fh.write(contents)
    fh.close()
def read_from_file(file_name):
    fh = open(file_name, 'r')
    contents = fh.read()
    fh.close()
    return contents

@app.route('/updateuserinfo', methods=["POST"])
def updateuserinfo():
    cccc=request.get_json()
    new_name = cccc.get("username")
    nickname = cccc.get("nickname")
    gender =cccc.get("gender")
    phone_nb = cccc.get('phone_number')
    age = cccc.get('age')
    img = cccc.get('avatar')
    if nickname=='-1':
        nickname = None
    if gender=='-1':
        gender = None
    if phone_nb=='-1':
        phone_nb = None
    if age==-1:
        age = None
    if img=='-1':
        img = None
    
    else:
        save_to_file('images/'+new_name+'.txt', img)
    try:
        update_usr_info(usr_name=new_name,
                                    usr_gender=gender,
                                    usr_phone_num=phone_nb,
                                    usr_nickname=nickname,
                                    usr_photo_location='images/'+new_name+'.txt',
                                    usr_age=age,
                                    usr_school=None,
                                    usr_email=None
                                    )
        return jsonify({'code':1})

    except:
        return jsonify({'code':2})

@app.route('/getavatar', methods=["GET"])
def getavatar():
    usr_name = request.args.get("username")
    try:
        contents = read_from_file('images/'+usr_name+'.txt')
        return jsonify({'code':1,'image':contents})
    except:
        return jsonify({'code':1,'image':""})

@app.route('/getuserinfo', methods=["GET"])
def getuserinfo():
    usr_name = request.args.get("username")
    userid=get_id_from_name(usr_name)
    if userid==None: return jsonify({'code':0})
    info=get_usr_info_by_id(userid)
    if info['nickname']==None:
        nickname=-1
    else: nickname=info['nickname']
    nickname
    try:
        contents = read_from_file('images/'+usr_name+'.txt')
        
    except:
        return jsonify({'code':0,'nickname':nickname, 'avatar':[], 'gender':info['gender'], 'phone_number':str(info['phone_num']),'age':info['age']})
    
    return jsonify({'code':1,'nickname':nickname,'avatar':contents, 'gender':info['gender'], 'phone_number':str(info['phone_num']),'age':info['age']})


@app.route('/newpost', methods=["GET"])
def newpost():
    usr_name = request.args.get("username")
    usrid=get_id_from_name(usr_name)
    if usrid is None:
        return jsonify({'code':2,'info':'username wrong'})
    title = request.args.get("title")
    content = request.args.get("content")
    catagory = request.args.get("group_name")
    try:
        type=catalogues.index(catagory)
    except:
        return jsonify({'code':2,'info':'category wrong'})
    post_id=add_new_post(usrid,title,content,type)
    return jsonify({'code':1, 'post_id':post_id})

@app.route('/newcomment', methods=["GET"])
def newcomment():
    usr_name = request.args.get("username")
    usrid=get_id_from_name(usr_name)
    if usrid is None:
        return jsonify({'code':2,'info':'username wrong'})
    refpostid = request.args.get("post_id")
    content = request.args.get("content")
    comment_id=add_new_comment(get_father_id_from_post_id(refpostid), usrid, content, refpostid)
    return jsonify({'code':1, 'post_id':comment_id})

@app.route('/search', methods=["POST","GET"])
def search():
    keywordsj = request.get_json()
    keywords=keywordsj['keywords']
    posts=get_allposts()
    entry=[]
    for post in posts:
        tmp={}
        flag=True
        for kw in keywords:
            if kw not in post[4] and kw not in post[5]:
                flag=False
                break
        if flag:
            tmp['post_id']=post[0]
            tmp['title']=post[4]
            tmp['group_name']=catalogues[post[7]] 
            tmp['content']=post[5]
            entry.append(tmp)

    return jsonify({'code':1,'number':len(entry),'entry':entry})


if __name__ == '__main__':
    app.config['JSON_AS_ASCII'] = False
    app.run(host='0.0.0.0',port=3000,debug=True)
