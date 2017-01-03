package example.caobin.com.myhandwrite;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private LinePathView mLinePathView;//签名view
    private Button clearBtn;//清屏按钮
    private EditText keyboardInputEdit;//键盘输入
    private CheckBox inputTypeCheckBox;//切换输入方式(手写/键盘)
    private int inputType = 1;//1手写（默认） 0键盘
    private ImageView inputHintView;//输入框提示背景图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        mLinePathView = (LinePathView) findViewById(R.id.path_view);
        clearBtn = (Button) findViewById(R.id.btn_clear_screen);
        keyboardInputEdit = (EditText) findViewById(R.id.edit_keyboard_register);
        inputTypeCheckBox = (CheckBox) findViewById(R.id.check_input_type);
        inputHintView = (ImageView) findViewById(R.id.img_view_hand_write_hint);
        findViewById(R.id.btn_dialog_close).setOnClickListener(this);
        findViewById(R.id.btn_save).setOnClickListener(this);
        keyboardInputEdit.setOnClickListener(this);
        clearBtn.setOnClickListener(this);
        //显示当前日期
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        ((TextView) findViewById(R.id.tv_date)).setText(sdf.format(c.getTime()));
    }

    /**
     * 监听事件
     */
    private void initListener() {
        //判断签名框是否有签名，改变清除按钮背景和隐藏签名提示背景
        mLinePathView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                inputHintView.setVisibility(View.INVISIBLE);
                clearBtn.setBackgroundResource(R.drawable.btn_clear_screen_selector);
                return false;
            }
        });
        //切换输入方式
        inputTypeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cleanScreen();
                //true手写  false键盘
                if (isChecked) {
                    keyboardInputEdit.setVisibility(View.INVISIBLE);
                    mLinePathView.setVisibility(View.VISIBLE);
                    inputType = 1;
                } else {
                    keyboardInputEdit.setVisibility(View.VISIBLE);
                    mLinePathView.setVisibility(View.INVISIBLE);
                    inputType = 0;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_dialog_close: {
                //关闭
                finish();
            }
            break;
            case R.id.edit_keyboard_register: {
                //edittext点击
                inputHintView.setVisibility(View.INVISIBLE);
                clearBtn.setBackgroundResource(R.drawable.btn_clear_screen_selector);
            }
            break;
            case R.id.btn_save: {
                //保存
                save();
            }
            break;
            case R.id.btn_clear_screen: {
                //清屏
                cleanScreen();
            }
            break;
            default:
                break;
        }
    }

    /**
     * 清屏
     */
    private void cleanScreen() {
        mLinePathView.clear();
        keyboardInputEdit.setText(null);
    }


    private String name = "";//键盘输入姓名,当然你可以自定义保存的方式

    /**
     * 保存信息
     */
    private void save() {
        if (inputType == 1) {
            //手写
            if (!mLinePathView.getTouched()) {
                //没有输入就点击保存，给用户提示
                toast("请签写你的姓名！");
            } else {
                //如果已经签写内容,进行保存
                saveImage("image");
            }

        } else {
            //键盘
            if (TextUtils.isEmpty(keyboardInputEdit.getText())) {
                //没有输入就点击保存，给用户提示
                toast("请输入你的姓名!");
            } else {
                //如果已经签写内容,进行保存,保存的方式可以自定义,这里就只保存在类变量里。
                name = keyboardInputEdit.getText().toString();
                toast("保存的姓名为：" + name);
            }
        }
    }

    /**
     * 保存签字图片
     *
     * @param imageName 保存的名称
     */
    private void saveImage(String imageName) {
        //图片保存的路径. 我的手机是小米
        //图片在data/saveImagePath目录下
        String imagePath = Constants.FilePath + File.separator + imageName + ".png";
        try {
            mLinePathView.save(imagePath, false, 0);
            toast("保存成功!");
            //保存成功后清屏
            cleanScreen();
        } catch (IOException e) {
            toast("保存失败!");
        }
    }

    /**
     * Toast任何类型的数据
     *
     * @param object
     */
    private void toast(Object object) {
        Toast.makeText(this, object.toString(), Toast.LENGTH_SHORT).show();
    }
}
