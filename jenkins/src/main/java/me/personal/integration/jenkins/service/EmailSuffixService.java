package me.personal.integration.jenkins.service;

public class EmailSuffixService {


    /**
     * 返回全限邮箱
     * 比如: a,b -> a@ppdai.com,b@ppdai.com
     * @param prefix
     * @return
     */
    public String addEmailSuffix(String prefix){

        String mailSuffix = "@ppdai.com";
        String receiverList = "";
        String[] receivers = prefix.split(",");
        int size = receivers.length;
        for (String receiver: receivers) {
            size--;
            if(size != 0) {
                receiverList += (receiver + mailSuffix) + ",";
            }else {
                receiverList += (receiver + mailSuffix);
            }

        }

        return receiverList;


    }


}
