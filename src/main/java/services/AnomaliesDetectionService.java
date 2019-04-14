package services;

import dao.history.processed.GraphPatternsDao;
import dao.history.processed.interfaces.ProcessedDaoFactory;
import database.generated.public_.tables.pojos.GraphPattern;
import database.generated.public_.tables.pojos.GraphPatternStructureAnomaly;
import javafx.util.Pair;
import utils.Utils;

import java.util.List;

public class AnomaliesDetectionService {

    private ProcessedDaoFactory processedDaoFactory;

    private Thread analysingThread;

    private static final int MAX_ANOMALY_DISTANCE = 1;

    private static final double MAX_ANOMALY_TO_NORMAL_OCCURRENCE_RATIO = 0.3;

    public AnomaliesDetectionService(ProcessedDaoFactory processedDaoFactory) {
        this.processedDaoFactory = processedDaoFactory;
    }

    public void startDetectingAnomalies() {
        analysingThread = new Thread(() -> {
            detectGraphStructureAnomalies();
        });
        analysingThread.start();
    }

    private void detectGraphStructureAnomalies() {
        GraphPatternsDao graphPatternsDao = processedDaoFactory.getGraphPatternsDao();

        int i = 0;
        GraphPattern graphPattern = graphPatternsDao.fetchOneByPatternId(i);

        while (graphPattern != null) {
            int j = i + 1;
            GraphPattern graphPatternToCompareWith = graphPatternsDao.fetchOneByPatternId(j);

            while (graphPatternToCompareWith != null) {
                compareGraphPatterns(graphPattern, graphPatternToCompareWith);
                graphPatternToCompareWith = graphPatternsDao.fetchOneByPatternId(++j);
            }

            graphPattern = graphPatternsDao.fetchOneByPatternId(++i);
        }
    }

    private void compareGraphPatterns(GraphPattern graphPattern, GraphPattern graphPatternToCompareWith) {
        GraphPatternsDao graphPatternsDao = processedDaoFactory.getGraphPatternsDao();
        int graphPatternOccurrenceCount = graphPattern.getOccurrenceCount();
        int graphPatternToCompareWithOccurrenceCount = graphPatternToCompareWith.getOccurrenceCount();

        double occurrenceRatio = (double) graphPatternOccurrenceCount / (double) graphPatternToCompareWithOccurrenceCount;
        if (occurrenceRatio > 1)
            occurrenceRatio = (double) 1 / occurrenceRatio;

        if (occurrenceRatio <= MAX_ANOMALY_TO_NORMAL_OCCURRENCE_RATIO) {
            int graphPatternDistance = getDistanceBetweenGraphPatterns(graphPattern, graphPatternToCompareWith);
            if (graphPatternDistance <= MAX_ANOMALY_DISTANCE) {
                int anomalyGraphPatternId = graphPattern.getPatternId();
                int relatedGraphPatternId = graphPatternToCompareWith.getPatternId();

                if (graphPatternOccurrenceCount > graphPatternToCompareWithOccurrenceCount) {
                    anomalyGraphPatternId = graphPatternToCompareWith.getPatternId();
                    relatedGraphPatternId = graphPattern.getPatternId();
                }

                GraphPatternStructureAnomaly anomaly = new GraphPatternStructureAnomaly();
                anomaly.setAnomalyGraphPatternId(anomalyGraphPatternId);
                anomaly.setRelatedGraphPatternId(relatedGraphPatternId);
                processedDaoFactory.getGraphPatternsStructureAnomaliesDao().insert(anomaly);
            }
        }
    }

    private int getDistanceBetweenGraphPatterns(GraphPattern graphPattern, GraphPattern graphPatternToCompareWith) {

        GraphPatternsDao graphPatternsDao = processedDaoFactory.getGraphPatternsDao();

        int maxMinDistance = 0;
        int i = 0;
        List<Integer> graphPatternTracePatternLabels
                = graphPatternsDao.getGraphPatternTracePatternMessageLabels(graphPattern.getPatternId(), i);

        while (graphPatternTracePatternLabels.size() > 0) {
            int minDistance = -1;
            int j = 0;
            List<Integer> anotherGraphPatternTracePatternLabels
                    = graphPatternsDao.getGraphPatternTracePatternMessageLabels(graphPatternToCompareWith.getPatternId(), j);

            while (graphPatternTracePatternLabels.size() > 0) {
                int distance = Utils.getLevenshteinDistanceBetweenLists(
                        graphPatternTracePatternLabels,
                        anotherGraphPatternTracePatternLabels
                );
                if (minDistance == -1 || distance < minDistance)
                    minDistance = distance;

                anotherGraphPatternTracePatternLabels
                        = graphPatternsDao.getGraphPatternTracePatternMessageLabels(graphPatternToCompareWith.getPatternId(), ++j);
            }

            if (minDistance > maxMinDistance)
                maxMinDistance = minDistance;

            graphPatternTracePatternLabels
                    = graphPatternsDao.getGraphPatternTracePatternMessageLabels(graphPattern.getPatternId(), i);
        }

        return maxMinDistance;
    }
}
