//
//  MoreAnimeVM.swift
//  iosApp
//
//  Created by Coder on 10/31/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class MoreAnimeVM: ObservableObject {
    @Published var list = [[AnimeUI]]()
    let vm = MoreAnimeViewModel()
    
    init(listUI: AnimeListUI) {
        var animeList = [Anime]()
        listUI.list.forEach { it in
            animeList.append(it.item)
        }
        vm.data.watch { listEmitted in
            var tempList = [AnimeUI]()
            listEmitted!.forEach { itemEmitted in
                tempList.append(AnimeUI(item: itemEmitted as! Anime))
            }
            self.list = tempList.chunked(into: 3)
        }
        vm.setInfo(list: animeList, loadMoreInfo: listUI.loadMoreInfo, loadedPage: Int32(listUI.page))
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
