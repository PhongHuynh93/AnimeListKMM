//
//  HomeAnimeVM.swift
//  iosApp
//
//  Created by Coder on 10/26/20.
//  Copyright © 2020 orgName. All rights reserved.
//

import Foundation
import shared

class HomeAnimeVM: ObservableObject {
    @Published var homeList = [HomeUI]()
    let homeVM: HomeAnimeViewModel
    
    init(homeVM: HomeAnimeViewModel) {
        self.homeVM = homeVM
        homeVM.data.watch { list in
            var tempHomeList = [HomeUI]()
            list!.forEach { it in
                switch it {
                case is AnimeList:
                    // convert nsarray into array
                    let oldList = it as! AnimeList
                    var list = [AnimeUI]()
            
                    oldList.list.forEach { it in
                        list.append(AnimeUI(item: it))
                    }
                    
                    tempHomeList.append(HomeUI(home: AnimeListUI(title: oldList.title, list: list, loadMoreInfo: oldList.loadMoreInfo, page: Int(oldList.page))))
                default:
                    break
                }
               
            }
            self.homeList = tempHomeList
        }
    }
    
    deinit {
        homeVM.onCleared()
    }
    
    func onItemAppear(itemId: UUID) {
        print("onItemAppear")
        if (homeList.last!.id == itemId) {
            print("loadmore")
            homeVM.loadMore()
        }
    }
}
