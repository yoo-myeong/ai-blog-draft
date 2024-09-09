package com.kotlin.aiblogdraft.api.domain

import com.kotlin.aiblogdraft.api.domain.draft.AppendDraft
import com.kotlin.aiblogdraft.api.domain.draft.DraftAppender
import com.kotlin.aiblogdraft.api.domain.draftImage.AppendImageResult
import com.kotlin.aiblogdraft.api.domain.draftImage.DraftImageAppender
import com.kotlin.aiblogdraft.api.domain.draftKey.AppendDraftKey
import com.kotlin.aiblogdraft.api.domain.draftKey.DraftKeyAppender
import com.kotlin.aiblogdraft.api.domain.draftKey.DraftKeyFinder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class DraftService(
    private val draftKeyAppender: DraftKeyAppender,
    private val draftKeyFinder: DraftKeyFinder,
    private val draftImageAppender: DraftImageAppender,
    private val draftAppender: DraftAppender,
) {
    fun createDraftKey(userId: Long): String {
        val draftKey = draftKeyAppender.appendKey(AppendDraftKey(userId))
        return draftKey
    }

    fun appendImages(
        key: String,
        files: Array<MultipartFile>,
        userId: Long,
    ): List<AppendImageResult> {
        val draftKey = draftKeyFinder.getValidDraftKey(key, userId).key
        val result = draftImageAppender.appendImages(draftKey, files)
        return result
    }

    fun requestDraft(
        userId: Long,
        appendDraft: AppendDraft,
    ): Long {
        draftKeyFinder.getValidDraftKey(appendDraft.key, userId).key
        return draftAppender.append(appendDraft).id
    }
}
