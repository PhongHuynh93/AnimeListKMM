//
//  MoreMangaVM.swift
//  iosApp
//
//  Created by Coder on 10/31/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class MoreMangaVM: ObservableObject {
    @Published var list = [[MangaUI]]()
    let vm = MoreMangaViewModel()
    
    init(mangaListUI: MangaListUI) {
        var mangaList = [Manga]()
        mangaListUI.mangaList.forEach { it in
            mangaList.append(it.manga)
        }
        vm.data.watch { listEmitted in
            var tempList = [MangaUI]()
            listEmitted!.forEach { itemEmitted in
                tempList.append(MangaUI(manga: itemEmitted as! Manga))
            }
            self.list = tempList.chunked(into: 3)
        }
        vm.setInfo(list: mangaList, loadMoreInfo: mangaListUI.loadMoreInfo, loadedPage: Int32(mangaListUI.page))
    }
    
    deinit {
        vm.onCleared()
    }
    
    func onItemAppear(itemId: UUID) {
        print("onItemAppear")
        if (list.last!.last!.id == itemId) {
            print("loadmore")
            vm.loadMore()
        }
    }
}
