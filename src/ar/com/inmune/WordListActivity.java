package ar.com.inmune;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ar.com.inmune.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import ar.com.inmune.deutsch.Article;
import ar.com.inmune.deutsch.Noun;
import ar.com.inmune.deutsch.Verb;
import ar.com.inmune.deutsch.Word;

public class WordListActivity extends Activity implements OnItemClickListener {

	public static final String EXTRA_TYPE = "type";
	public static final String TYPE_VERB = "verb";
	public static final String TYPE_NOUN = "noun";
	public static final String TYPE_ARTICLE = "article";

	private static List<Verb> verbs;
	private static List<Noun> nouns;
	private static List<Article> articles;

	private List<? extends Word> currentData;
	private Word wordData[];
	
	public class WordAdapter extends ArrayAdapter<Word> {
	    private Context context; 
	    private int layoutResourceId;    
	    private Word data[] = null;

		public WordAdapter(Context context, int layoutResourceId, Word[] data) {
			super(context, layoutResourceId, data);
			
	        this.layoutResourceId = layoutResourceId;
	        this.context = context;
	        this.data = data;
		}

	    @Override
	    public View getView(int position, View view, ViewGroup parent) {
	        TextView tView;
	        
	        if (view == null) {
	            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	            view = inflater.inflate(layoutResourceId, parent, false);
	        }
            tView = (TextView)view.findViewById(R.id.word);
	        
	        final Word word = data[position];
	        tView.setText(word.toString());

	        view.findViewById(R.id.referenceButton).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					HelpActivity.showWordReference(context, word);
				}
			});

	        view.findViewById(R.id.externalReferenceButton).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					BrowserActivity.showExternalWordReference(context, word);
				}
			});        
	        return view;
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_list);

		Intent intent = getIntent();
		if (intent != null) {
			savedInstanceState = intent.getExtras();
		}
		
		String type = (String) savedInstanceState.get(EXTRA_TYPE);
		if (type.equals(TYPE_VERB)) {
			loadVerbs();
			currentData = verbs;
		} else if (type.equals(TYPE_NOUN)) {
			loadNouns();
			currentData = nouns;
		} else if (type.equals(TYPE_ARTICLE)) {
			loadArticles();
			currentData = articles;
		}
		wordData = currentData.toArray(new Word[]{});
		WordAdapter adapter = new WordAdapter(this, R.layout.word_reference_layout, wordData);
		ListView listView = (ListView)findViewById(R.id.wordList);
		listView.setAdapter(adapter);
		
		AutoCompleteTextView actv = (AutoCompleteTextView)findViewById(R.id.searchText);
		actv.setAdapter(adapter);
		
		listView.requestFocus();
	}

	protected List<Noun> loadNouns() {
		if (nouns == null) {
			nouns = GermanPractice.dataProvider.getNouns();
			Collections.sort(nouns);
		}
		return nouns;
	}

	protected List<Article> loadArticles() {
		if (articles == null) {
			articles = GermanPractice.dataProvider.getArticles();
			Collections.sort(articles);
		}
		return articles;
	}
	protected List<Verb> loadVerbs() {
		if (verbs == null) {
			verbs = GermanPractice.dataProvider.getVerbs();
			Collections.sort(verbs);
			verbs.addAll(0, Arrays.asList(Verb.MODAL_AUXILIARY));
			verbs.addAll(0, Arrays.asList(Verb.sein, Verb.haben, Verb.werden));
		}
		return verbs;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		//
	}
}
