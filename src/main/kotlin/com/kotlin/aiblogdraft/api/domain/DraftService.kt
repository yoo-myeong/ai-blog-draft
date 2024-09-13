package com.kotlin.aiblogdraft.api.domain

import com.kotlin.aiblogdraft.api.domain.draft.AppendDraft
import com.kotlin.aiblogdraft.api.domain.draft.DraftAppender
import com.kotlin.aiblogdraft.api.domain.draft.DraftContent
import com.kotlin.aiblogdraft.api.domain.draft.DraftReadResult
import com.kotlin.aiblogdraft.api.domain.draft.DraftReader
import com.kotlin.aiblogdraft.api.domain.draft.DraftStatus
import com.kotlin.aiblogdraft.api.domain.draft.DraftStatusResult
import com.kotlin.aiblogdraft.api.domain.draftImage.AppendImageResult
import com.kotlin.aiblogdraft.api.domain.draftImage.DraftImageSaver
import com.kotlin.aiblogdraft.api.domain.draftKey.AppendDraftKey
import com.kotlin.aiblogdraft.api.domain.draftKey.DraftKeyAppender
import com.kotlin.aiblogdraft.api.domain.draftKey.DraftKeyFinder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class DraftService(
    private val draftKeyAppender: DraftKeyAppender,
    private val draftKeyFinder: DraftKeyFinder,
    private val draftImageSaver: DraftImageSaver,
    private val draftAppender: DraftAppender,
    private val draftReader: DraftReader,
) {
    fun createKey(userId: Long): String {
        val draftKey = draftKeyAppender.appendKey(AppendDraftKey(userId))

        return draftKey
    }

    fun saveImages(
        key: String,
        files: Array<MultipartFile>,
        userId: Long,
    ): List<AppendImageResult> {
        val draftKey = draftKeyFinder.getValidDraftKey(key, userId).key
        val result = draftImageSaver.save(draftKey, files)

        return result
    }

    fun append(appendDraft: AppendDraft): Long {
        draftKeyFinder.getValidDraftKey(appendDraft.key, appendDraft.userId).key

        return draftAppender.append(appendDraft).id
    }

    fun status(userId: Long): List<DraftStatusResult> {
        val drafts = draftReader.readByUserId(userId)

        return drafts.map { DraftStatusResult(it.id, it.title, DraftStatus.findByStatus(it.status)) }
    }

    fun read(id: Long): DraftReadResult {
        val draft = draftReader.readById(id)

        return DraftReadResult.fromDraftEntity(draft)
    }

    fun content(id: Long): DraftContent {
        val draft = draftReader.readDoneById(id)
        return DraftContent.fromDraftEntity(draft)
    }
}
