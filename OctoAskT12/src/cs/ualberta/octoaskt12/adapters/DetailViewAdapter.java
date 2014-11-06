package cs.ualberta.octoaskt12.adapters;

import java.util.ArrayList;
import java.util.List;

import cs.ualberta.octoaskt12.Answer;
import cs.ualberta.octoaskt12.AnswerHolder;
import cs.ualberta.octoaskt12.CreateAnswerActivity;
import cs.ualberta.octoaskt12.CreateReplyActivity;
import cs.ualberta.octoaskt12.MainActivity;
import cs.ualberta.octoaskt12.Question;
import cs.ualberta.octoaskt12.QuestionHolder;
import cs.ualberta.octoaskt12.R;
import cs.ualberta.octoaskt12.Reply;
import cs.ualberta.octoaskt12.UserArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetailViewAdapter extends BaseExpandableListAdapter {


	private static final int CREATE_REPLY_ACTIVITY_CODE = 1236;
	private Context context;
	private Question question;
	private ArrayList<Answer> answers;
	private ArrayList<Reply> replyList;
	
	public DetailViewAdapter(Context context, Question question) {
		super();
		this.context = context;
		this.question = question;
		this.answers = question.getAnswers();
	}
	
	// return a child item
	@Override
	public Reply getChild(int groupPosition, int childPosition) {
		if (groupPosition == 0) {
			return this.question.getReplies().get(childPosition);
		} else {
			return this.answers.get(groupPosition - 1).getReplies()
					.get(childPosition);
		}
	}
	// return item's id
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}
	
	
	
	// return view for each item row
	@Override
	public View getChildView(final int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
	
		if (groupPosition == 0) {
			final String replyBody = getChild(groupPosition, childPosition)
					.getBody();

			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.detail_question_replies,null);
 
			TextView questionReply = (TextView) convertView
					.findViewById(R.id.detail_question_replies);
			questionReply.setText(replyBody);

			return convertView;
		} else {
			
			if (this.answers.get(groupPosition -1).getReplies().size() == 0){
				LayoutInflater inflater = (LayoutInflater) this.context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.detail_answer_replies_button, null);
				
				TextView answerReply = (TextView) convertView.findViewById(R.id.detail_answer_replies);
				answerReply.setText("This Answer has no replies. Be the first to reply.");
				Button replyButton = (Button) convertView.findViewById(R.id.detail_answer_replies_add);
				
				replyButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context, CreateReplyActivity.class);
						AnswerHolder answerHolder = AnswerHolder.getInstance();
						answerHolder.setAnswer(answers.get(groupPosition-1));
						((Activity)context).startActivity(intent);
					}
				});
				
			}
			else{
				String replyBody = getChild(groupPosition, childPosition).getBody();
				if (isLastChild) {
					LayoutInflater inflater = (LayoutInflater) this.context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.detail_answer_replies_button, null);
					Button replyButton = (Button) convertView.findViewById(R.id.detail_answer_replies_add);
					
					replyButton.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(context, CreateReplyActivity.class);
							AnswerHolder answerHolder = AnswerHolder.getInstance();
							answerHolder.setAnswer(answers.get(groupPosition-1));
							((Activity)context).startActivity(intent);
						}
					});
				} else {
					LayoutInflater inflater = (LayoutInflater) this.context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = inflater.inflate(R.layout.detail_answer_replies,null);
				}
				TextView answerReply = (TextView) convertView.findViewById(R.id.detail_answer_replies);
				answerReply.setText(replyBody);
				}
			
			
			return convertView;
		}
		
	}
	
	
	
	
	// return number of items in each section
	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition == 0) {
			return question.getReplies().size();
		} else {
			int replySize = this.answers.get(groupPosition - 1).getReplies().size();
			if (replySize == 0){return 1;};
			return replySize;
		}
	} 
	// return sections
	@Override
	public Object getGroup(int groupPosition) {
		if (groupPosition == 0) {
			return this.question;
		} else {
			return this.answers.get(groupPosition - 1);
		}
	}
	// return the number of sections
	@Override
	public int getGroupCount() {
		return this.answers.size() + 1;
	}

	// return a section's id
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// retrun a view for each section header
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (groupPosition == 0) {
			String questionTitle = question.getTitle();
			String questionBody = question.getBody();
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.detail_question_header,
					null);
			TextView questionBodyTextView = (TextView) convertView
					.findViewById(R.id.detail_question_body);
			TextView questionTitleTextView = (TextView) convertView
					.findViewById(R.id.detail_question_header);
			questionBodyTextView.setText(questionBody);
			questionTitleTextView.setText(questionTitle);
			return convertView;
		} else {
			String answerBody = answers.get(groupPosition - 1).getBody();
			LayoutInflater inflater = (LayoutInflater) this.context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.detail_answer_header, null);
			TextView answerBodyTextView = (TextView) convertView
					.findViewById(R.id.detail_answer_header);
			if (answerBodyTextView == null) {
				System.out.println("null");
			}
			answerBodyTextView.setText(answerBody);

			return convertView;
		}

	}


	





	@Override
	public boolean hasStableIds() {
		return false;
	}

	

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

}
