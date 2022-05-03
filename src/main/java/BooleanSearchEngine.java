import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class BooleanSearchEngine implements SearchEngine {

    private Map<String, List<PageEntry>> allWords = new HashMap<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {
        File[] files = pdfsDir.listFiles();
        assert files != null;
        for (File file : files) {
            var doc = new PdfDocument(new PdfReader(file));
            int countPage = doc.getNumberOfPages();
            for (int j = 1; j <= countPage; j++) {
                var text = PdfTextExtractor.getTextFromPage(doc.getPage(j));
                var words = text.split("\\P{IsAlphabetic}+");

                Map<String, Integer> freqs = new HashMap<>();
                for (var word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    freqs.put(word.toLowerCase(), freqs.getOrDefault(word, 0) + 1);
                }

                for (Map.Entry<String, Integer> entry : freqs.entrySet()) {
                    PageEntry entryWord = new PageEntry(file.getName(), j, entry.getValue());
                    if (allWords.containsKey(entry.getKey())) {
                        allWords.get(entry.getKey()).add(entryWord);
                    } else {
                        List<PageEntry> uniqueWords = new ArrayList<>();
                        uniqueWords.add(entryWord);
                        allWords.put(entry.getKey(), uniqueWords);
                    }
                }
            }
        }
    }

    public Map<String, List<PageEntry>> getAllWords() {
        return allWords;
    }

    public void setAllWords(Map<String, List<PageEntry>> allWords) {
        this.allWords = allWords;
    }

    @Override
    public List<PageEntry> search(String word) {
        if (allWords.containsKey(word)) {
            List<PageEntry> result = allWords.get(word);
            Collections.sort(result);
            return result;
        } else {
            return Collections.emptyList();
        }
    }
}