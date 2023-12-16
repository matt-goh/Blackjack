package com.example.assignment1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class blackjack extends AppCompatActivity {
    Random random = new Random();
    int userSum = 0;
    int pcSum = 0;
    public int deal_cards(){
        int [] cards = {11, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};

        // generate a random index within the array's length
        int randomIndex = random.nextInt(cards.length);

        // extract and return the random number from the array
        return cards[randomIndex];
    }
    public static int calculateSum(List<Integer> list){ // sum up the list and replace 11 with 1 if the sum is over 21
        int sum = list.stream().mapToInt(Integer::intValue).sum();
        if (list.contains(11) && sum > 21) {
            list.set(list.indexOf(11), 1);
            sum = list.stream().mapToInt(Integer::intValue).sum();
        }
        return sum;
    }
    public void game_over(List<Integer> pc, int pcSum, int newUserSum){
        final ImageView card = findViewById(R.id.card);
        final ImageView pc2 = findViewById(R.id.pc2);
        final TextView pcSumDisplay = findViewById(R.id.pcSumDisplay);
        final TextView game_result = findViewById(R.id.game_result);
        final Button play_again = findViewById(R.id.btnPlay_Again);
        final Button drawButton = findViewById(R.id.btnDraw);
        final Button passButton = findViewById(R.id.btnPass);

        card.setVisibility(View.INVISIBLE); // reveal the pc's second card
        pc2.setVisibility(View.VISIBLE);

        while (pcSum != 0 && pcSum < 17) { // pc deal cards until sum is over 17
            pc.add(deal_cards());
            ImageView pcDrawn = findViewById(R.id.pcDrawn);
            pcSum = calculateSum(pc);
            setImage(pcDrawn, pc.get(pc.size()-1)); // set the drawn card image to the latest card
            pcDrawn.setVisibility(View.VISIBLE); // display the drawn card
        }

        pcSumDisplay.setText(String.valueOf(pcSum)); // display sum of pc's cards

        if (newUserSum > 21 && pcSum > 21) // calculate the score and set the text for "game_result"
            game_result.setText(R.string.lose);
        if (newUserSum == pcSum)
            game_result.setText(R.string.draw);
        else if (pcSum == 21)
            game_result.setText(R.string.lose);
        else if (newUserSum == 21)
            game_result.setText(R.string.win);
        else if (newUserSum > 21)
            game_result.setText(R.string.lose);
        else if (pcSum > 21)
            game_result.setText(R.string.win);
        else if (newUserSum > pcSum)
            game_result.setText(R.string.win);
        else
            game_result.setText(R.string.lose);

        game_result.setVisibility(View.VISIBLE); // display the result

        drawButton.setVisibility(View.INVISIBLE);
        passButton.setVisibility(View.INVISIBLE);
        play_again.setVisibility(View.VISIBLE);

        play_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(blackjack.this, blackjack.class));
            }
        });

    }
    public void setImage(ImageView imageView, int card) {
        switch (card) {
            case 11:
                imageView.setImageResource(R.drawable.ace);
                break;
            case 2:
                imageView.setImageResource(R.drawable.two);
                break;
            case 3:
                imageView.setImageResource(R.drawable.three);
                break;
            case 4:
                imageView.setImageResource(R.drawable.four);
                break;
            case 5:
                imageView.setImageResource(R.drawable.five);
                break;
            case 6:
                imageView.setImageResource(R.drawable.six);
                break;
            case 7:
                imageView.setImageResource(R.drawable.seven);
                break;
            case 8:
                imageView.setImageResource(R.drawable.eight);
                break;
            case 9:
                imageView.setImageResource(R.drawable.nine);
                break;
            case 10:
                int randomNum = random.nextInt(4);
                switch (randomNum) {
                    case 0:
                        imageView.setImageResource(R.drawable.ten);
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.j);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.q);
                        break;
                    case 3:
                        imageView.setImageResource(R.drawable.k);
                        break;
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(blackjack.this, MainActivity.class));
        finish(); // Close the current activity
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blackjack);

        Button backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(blackjack.this, MainActivity.class));
                finish();
            }
        });

        final ImageView pc1 = findViewById(R.id.pc1);
        final ImageView pc2 = findViewById(R.id.pc2);
        final ImageView user1 = findViewById(R.id.user1);
        final ImageView user2 = findViewById(R.id.user2);
        final ImageView userDrawn = findViewById(R.id.userDrawn);
        final TextView userSumDisplay = findViewById(R.id.userSumDisplay);
        final TextView pcSumDisplay = findViewById(R.id.pcSumDisplay);
        final Button drawButton = findViewById(R.id.btnDraw);
        final Button passButton = findViewById(R.id.btnPass);

        List<Integer> user = new ArrayList<>();
        List<Integer> pc = new ArrayList<>();

        for (int i = 0; i < 2; i++) { // deal the user and pc 2 cards each using for loop
            user.add(deal_cards());
            pc.add(deal_cards());
        }

        setImage(pc1, pc.get(0)); // set all the images using the setImage function that
        setImage(pc2, pc.get(1)); // assigns the correct card image according to the integer
        setImage(user1, user.get(0));
        setImage(user2, user.get(1));

        userSum = calculateSum(user);
        pcSum = calculateSum(pc);

        userSumDisplay.setText(String.valueOf(userSum)); // display the sum of the cards
        pcSumDisplay.setText(String.valueOf(pc.get(0)));

        if (userSum == 21 || pcSum == 21){ // check if there is blackjack in the first 2 draws
            game_over(pc, pcSum, userSum);
        }
        else { // if no blackjack then allow user to draw
            drawButton.setOnClickListener(new View.OnClickListener() { // draw button
                @Override
                public void onClick(View v) {
                    user.add(deal_cards());
                    setImage(userDrawn, user.get(user.size()-1));
                    userSum = calculateSum(user);
                    userSumDisplay.setText(String.valueOf(userSum));
                    userDrawn.setVisibility(View.VISIBLE);
                }
            });

            passButton.setOnClickListener(new View.OnClickListener() { // pass button
                @Override
                public void onClick(View v) {
                    game_over(pc, pcSum, userSum);
                }
            });
        }
    }
}