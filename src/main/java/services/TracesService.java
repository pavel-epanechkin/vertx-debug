package services;

import dao.LimitParams;
import dao.history.processed.TracesDao;
import database.generated.public_.tables.pojos.Message;
import database.generated.public_.tables.pojos.Trace;
import dto.db.TracePartInfo;
import dto.requests.PaginationParams;
import dto.responses.ListDTO;
import dto.responses.MessageInfoDTO;
import dto.responses.TraceInfoDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TracesService {

    private TracesDao tracesDao;

    public TracesService(TracesDao tracesDao) {
        this.tracesDao = tracesDao;
    }

    public ListDTO getTracesList(PaginationParams paginationParams) throws IOException {
        int pageNumber = paginationParams.getPageNumber();
        int itemsOnPage = paginationParams.getItemsOnPage();
        int offset = pageNumber*itemsOnPage;

        List<Trace> traces = tracesDao.getByLikeFilterWithLimit(
                paginationParams.getFilter(),
                new LimitParams(offset, itemsOnPage)
        );
        int totalCount = tracesDao.getCountByLikeFilter(
                paginationParams.getFilter()
        );

        List<TraceInfoDTO> resultData = getTracesExtendedInfo(traces);

        ListDTO resultDto = new ListDTO(pageNumber, itemsOnPage, totalCount,
                paginationParams.getFilter(), resultData);

        return resultDto;
    }

    private List<TraceInfoDTO> getTracesExtendedInfo(List<Trace> traces) {
        List<Integer> traceIds = traces.stream()
                .map(Trace::getTraceId)
                .collect(Collectors.toList());
        List<TracePartInfo> tracesPartInfos = tracesDao.getTracesPartsInfo(traceIds);

        return prepareTracesInfoDto(traces, tracesPartInfos);
    }



    public ListDTO getTracesForMessage(Integer messageId, PaginationParams paginationParams) throws IOException {
        int pageNumber = paginationParams.getPageNumber();
        int itemsOnPage = paginationParams.getItemsOnPage();
        int offset = pageNumber*itemsOnPage;

        List<Trace> traces = tracesDao.getTracesForMessage(
                messageId,
                new LimitParams(offset, itemsOnPage)
        );
        int totalCount = tracesDao.getTracesForMessageCount(messageId);

        List<TraceInfoDTO> resultData = getTracesExtendedInfo(traces);

        ListDTO resultDto = new ListDTO(pageNumber, itemsOnPage, totalCount,
                paginationParams.getFilter(), resultData);

        return resultDto;
    }



    private List<TraceInfoDTO> prepareTracesInfoDto(List<Trace> traces, List<TracePartInfo> tracesPartInfos) {
        List<TraceInfoDTO> traceInfoDTOS = new ArrayList<>();
        TraceInfoDTO traceInfoDTO = new TraceInfoDTO();

        for (int i = 0; i < tracesPartInfos.size();) {
            TracePartInfo tracePartInfo = tracesPartInfos.get(i);
            Integer traceId = tracePartInfo.getTraceId();

            if (traceInfoDTO.getTraceId() == null) {
                traceInfoDTO.setTraceId(traceId);
                traceInfoDTO.setTraceLabels(new ArrayList<>());
                traceInfoDTO.setStartTime(tracePartInfo.getStartTime().toString());
                traceInfoDTO.setEndTime(tracePartInfo.getEndTime().toString());
                traceInfoDTOS.add(traceInfoDTO);
            }

            if (traceInfoDTO.getTraceId().equals(traceId)) {
                traceInfoDTO.getTraceLabels().add(tracePartInfo.getFirstMessageLabel());
                traceInfoDTO.getTraceLabels().add(tracePartInfo.getLastMessageLabel());
                traceInfoDTO.setEndTime(tracePartInfo.getEndTime().toString());
                i++;
            }
            else {
                traceInfoDTO = new TraceInfoDTO();
            }
        }

        return traceInfoDTOS;
    }
}
