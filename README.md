# VerticalChipGroupAndroid
Custom view Vertical Chip Group for android, lays out chips in vertical order instead of default horizontal order in Chip Group

## Example Screenshot where rows specified = 2
![alt text](https://drive.google.com/uc?export=view&id=1gLJcjSA9RXORQvzOITISDfRtWFUIFaUH)

## Example Screenshot where rows specified = 3
![alt text](https://drive.google.com/uc?export=view&id=1XdNwgPgf-Op7i4i_PTIJnxFqVsvtn8bd)

## Add dependencies

In project level build.gradle-
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	} 
```
In app level build.gradle-
```
dependencies {
	        implementation 'com.github.amit12297:VerticalChipGroupAndroid:1.0'
	}
```

## activity_main.xml
```
      <com.av.verticalchipgroup.CustomChipGroup
        android:id="@+id/verticalChipGroup"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:rows="2"
        />
 ```   
 ## MainActivity.java
``` 
 public class MainActivity extends AppCompatActivity {

    private CustomChipGroup chipGroup;
    private List<String> payload = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        chipGroup = findViewById(R.id.verticalChipGroup);
        payload.add("Vertical - 1");
        payload.add("Chip - 2");
        payload.add("Group - 3");
        payload.add("Is - 4");
        payload.add("Great - 5");
        showData(payload);
    }

    private void showData(@NonNull List<String> payloadList) {
        if (chipGroup != null) {
            int size = payloadList.size();
            if(size > 0) {
                for (int i = 0; i < size; i++) {
                    Chip chip = new Chip(this);
                    chip.setText(payloadList.get(i));
                    chip.setTextColor(Color.parseColor("#666666"));
                    chipGroup.addView(chip);
                }
            }
        }
    }

}
```
