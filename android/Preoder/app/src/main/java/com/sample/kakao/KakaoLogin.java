package com.sample.kakao;

import android.app.Activity;

public class KakaoLogin extends Activity {
//    public static class KakaoSessionCallback implements ISessionCallback{
//        private Context mContext;
//        private MainActivity mainActivity;
//
//        public KakaoSessionCallback(Context context,MainActivity activity){
//            this.mContext=context;
//            this.mainActivity=activity;
//        }
//
//        @Override
//        public void onSessionOpened(){
//            requestMe();
//        }
//
//        @Override
//        public void onSessionOpenFailed(KakaoException e){
//            Toast.makeText(mContext,"KaKao 로그인 오류가 발생했습니다."+e.toString(),Toast.LENGTH_SHORT).show();
//        }
//        protected void requestMe(){
//            UserManagement.getInstance().me(new MeV2ResponseCallback() {//로그인 요청
//                @Override
//                public void onSessionClosed(ErrorResult errorResult) {//세션닫힘
//                    mainActivity.directToSecondActivity(false);
//                }
//
//                @Override
//                public void onSuccess(MeV2Response result){//로그인 요청했을때 로그인 성공했을 경우
//                    List<String> userInfo=new ArrayList<>();
//                    userInfo.add(String.valueOf(result.getId()));
//                    userInfo.add(result.getKakaoAccount().getProfile().getNickname());
//                    GlobalHelper mGlobalHelper=new GlobalHelper();
//                    GlobalHelper.setGlobalUserLoginInfo(userInfo);
//
//                    mainActivity.directToSecondActivity(true);
//                }
//            });
//        }
//    }
}
