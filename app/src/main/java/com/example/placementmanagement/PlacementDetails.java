package com.example.placementmanagement;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class PlacementDetails extends Fragment {
    private RecyclerView recyclerView;
    private PlacementDeptAdapter adapter;
    private List<PlacementUpd> placementList, selectedCompanies;
    private DatabaseReference databaseReference;
    private Button btnPrint;

    public PlacementDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placement_details, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewPlacement);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnPrint = view.findViewById(R.id.btnPrint);

        placementList = new ArrayList<>();
        selectedCompanies = new ArrayList<>();
        adapter = new PlacementDeptAdapter(placementList, selectedCompanies);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("placement_updates");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                placementList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    PlacementUpd placement = dataSnapshot.getValue(PlacementUpd.class);
                    if (placement != null) {
                        placementList.add(placement);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        btnPrint.setOnClickListener(v -> {
            if (selectedCompanies.isEmpty()) {
                Toast.makeText(getContext(), "Please select at least one company", Toast.LENGTH_SHORT).show();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    generatePDF();
                }
            }
        });

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void generatePDF() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(800, 1200, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Set background color for the page
        canvas.drawColor(Color.WHITE);

        // Draw header logo
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        Bitmap scaledLogo = Bitmap.createScaledBitmap(logo, 100, 100, false);
        canvas.drawBitmap(scaledLogo, (pageInfo.getPageWidth() - scaledLogo.getWidth()) / 2, 20, paint);

        paint.setTextSize(26);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(Color.BLACK);
        canvas.drawText("Placement Details", (pageInfo.getPageWidth() / 2) - 100, 150, paint);

        int x = 50, y = 200;
        int tableWidth = 700;
        int columnWidth = tableWidth / 4;

        // Header background
        paint.setColor(Color.LTGRAY);
        canvas.drawRect(x, y, x + tableWidth, y + 50, paint);
        paint.setColor(Color.BLACK);
        paint.setTextSize(18);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        // Headers
        canvas.drawText("Company Name", x + 10, y + 30, paint);
        canvas.drawText("Job Role", x + columnWidth + 10, y + 30, paint);
        canvas.drawText("Department", x + columnWidth * 2 + 10, y + 30, paint);
        canvas.drawText("Last Date", x + columnWidth * 3 + 10, y + 30, paint);
        y += 60;

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(16);

        for (PlacementUpd placement : selectedCompanies) {
            // Draw cell backgrounds for better readability
            paint.setColor(Color.WHITE);
            canvas.drawRect(x, y, x + tableWidth, y + 50, paint);
            paint.setColor(Color.BLACK);

            // Wrap text if too long
            drawWrappedText(canvas, paint, placement.getCompanyName(), x + 10, y + 30, columnWidth - 10);
            drawWrappedText(canvas, paint, placement.getJobRole(), x + columnWidth + 10, y + 30, columnWidth - 10);
            drawWrappedText(canvas, paint, placement.getDepartment(), x + columnWidth * 2 + 10, y + 30, columnWidth - 10);
            drawWrappedText(canvas, paint, placement.getLastDate(), x + columnWidth * 3 + 10, y + 30, columnWidth - 10);

            y += 60;
        }

        pdfDocument.finishPage(page);

        try {
            ContentResolver resolver = requireContext().getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "PlacementDetails.pdf");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);
            Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);
            if (uri != null) {
                OutputStream outputStream = resolver.openOutputStream(uri);
                pdfDocument.writeTo(outputStream);
                outputStream.close();
                Toast.makeText(requireContext(), "PDF saved successfully!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(requireContext(), "Error saving PDF", Toast.LENGTH_LONG).show();
        }

        pdfDocument.close();
    }

    // Function to wrap text properly inside table columns
    private void drawWrappedText(Canvas canvas, Paint paint, String text, int x, int y, int maxWidth) {
        int lineHeight = 20;
        int startY = y;
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (paint.measureText(line + " " + word) > maxWidth) {
                canvas.drawText(line.toString(), x, startY, paint);
                startY += lineHeight;
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) {
                    line.append(" ");
                }
                line.append(word);
            }
        }
        canvas.drawText(line.toString(), x, startY, paint);
    }
}
